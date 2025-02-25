package com.example.ecommerce.services;

import com.example.ecommerce.dto.ProductFilterDTO;
import com.example.ecommerce.exceptions.BadRequestException;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.repositories.ProductRepository;
import com.example.ecommerce.specifications.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UnsplashService unsplashService;

    public Page<Product> getAllProducts(int page, int size, String sortBy, String sortOrder) {
        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(sortOrder) ? Sort.Order.desc(sortBy) : Sort.Order.asc(sortBy)
        );
        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findAll(pageable);
    }


    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", id));
    }

    @Transactional
    public Product createProduct(Product product) {
        validateProduct(product);

        if (product.getImageUrl() == null || product.getImageUrl().isBlank()) {
            product.setImageUrl(unsplashService.getShoeImageUrl(product.getColor()));
        }

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        validateProduct(productDetails);

        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setDescription(productDetails.getDescription());
        product.setCategory(productDetails.getCategory());
        product.setBrand(productDetails.getBrand());
        product.setColor(productDetails.getColor());
        product.setSizes(productDetails.getSizes());

        if (!product.getColor().equalsIgnoreCase(productDetails.getColor())) {
            product.setImageUrl(unsplashService.getShoeImageUrl(productDetails.getColor()));
        }

        return productRepository.save(product);
    }

    public Page<Product> getFilteredProducts(ProductFilterDTO filterDTO, int page, int size) {
        Specification<Product> spec = Specification
                .where(ProductSpecifications.hasCategory(filterDTO.getCategory()))
                .and(ProductSpecifications.hasBrand(filterDTO.getBrand()))
                .and(ProductSpecifications.hasColor(filterDTO.getColor()))
                .and(ProductSpecifications.hasSize(filterDTO.getShoeSize()))
                .and(ProductSpecifications.priceBetween(filterDTO.getMinPrice(), filterDTO.getMaxPrice()));

        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(filterDTO.getSortOrder()) ? Sort.Order.desc(filterDTO.getSortBy()) : Sort.Order.asc(filterDTO.getSortBy())
        );

        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findAll(spec, pageable);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().isBlank()) {
            throw new BadRequestException("Product name cannot be empty.");
        }

        if (product.getPrice() < 0) {
            throw new BadRequestException("Price cannot be negative.");
        }

        if (product.getCategory() == null || product.getCategory().isBlank()) {
            throw new BadRequestException("Product category cannot be empty.");
        }

        if (product.getBrand() == null || product.getBrand().isBlank()) {
            throw new BadRequestException("Product brand cannot be empty.");
        }
    }
}
