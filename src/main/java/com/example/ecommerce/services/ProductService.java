package com.example.ecommerce.services;

import com.example.ecommerce.dto.ProductFilterDTO;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.repositories.ProductRepository;
import com.example.ecommerce.specifications.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UnsplashService unsplashService; // Inject UnsplashService

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public Product createProduct(Product product) {
        if (product.getImageUrl() == null || product.getImageUrl().isEmpty()) {
            product.setImageUrl(unsplashService.getShoeImageUrl(product.getColor()));
        }
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
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
                .and(ProductSpecifications.hasSize(filterDTO.getSize()))
                .and(ProductSpecifications.priceBetween(filterDTO.getMinPrice(), filterDTO.getMaxPrice()));

        Sort sort = filterDTO.getSortOrder().equalsIgnoreCase("desc")
                ? Sort.by(filterDTO.getSortBy()).descending()
                : Sort.by(filterDTO.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(spec, pageable);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
