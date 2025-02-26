package com.example.ecommerce.services;

import com.example.ecommerce.dto.FiltersDTO;
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

import java.util.Arrays;
import java.util.List;


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
                .where(ProductSpecifications.hasCategories(filterDTO.getCategories()))
                .and(ProductSpecifications.hasBrands(filterDTO.getBrands()))
                .and(ProductSpecifications.hasColors(filterDTO.getColors()))
                .and(ProductSpecifications.hasShoeSizes(filterDTO.getShoeSizes()))
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

    public FiltersDTO getProductFilterInfo() {
        Object[] result = (Object[]) productRepository.getFilterInfoNative();

        List<String> categories = result[0] != null ? Arrays.asList(result[0].toString().split(",")) : List.of();
        List<String> brands = result[1] != null ? Arrays.asList(result[1].toString().split(",")) : List.of();
        List<String> colors = result[2] != null ? Arrays.asList(result[2].toString().split(",")) : List.of();
        List<String> sizes = result[3] != null ? Arrays.asList(result[3].toString().split(",")) : List.of();
        Integer maxPrice = result[4] != null ? ((Number) result[4]).intValue() : 0;

        return new FiltersDTO(categories, brands, colors, sizes, maxPrice);
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
