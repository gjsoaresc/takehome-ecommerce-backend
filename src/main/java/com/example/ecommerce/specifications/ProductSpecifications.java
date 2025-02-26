package com.example.ecommerce.specifications;

import com.example.ecommerce.models.Product;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

import java.util.List;

public class ProductSpecifications {

    public static Specification<Product> hasCategories(List<String> categories) {
        return (root, query, criteriaBuilder) ->
                (categories == null || categories.isEmpty()) ? null : root.get("category").in(categories);
    }

    public static Specification<Product> hasBrands(List<String> brands) {
        return (root, query, criteriaBuilder) ->
                (brands == null || brands.isEmpty()) ? null : root.get("brand").in(brands);
    }

    public static Specification<Product> hasColors(List<String> colors) {
        return (root, query, criteriaBuilder) ->
                (colors == null || colors.isEmpty()) ? null : root.get("color").in(colors);
    }

    public static Specification<Product> hasShoeSizes(List<String> shoeSizes) {
        return (root, query, criteriaBuilder) -> {
            if (shoeSizes == null || shoeSizes.isEmpty()) return null;
            return root.join("sizes").in(shoeSizes);
        };
    }

    public static Specification<Product> priceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) return null;
            if (minPrice == null) return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            if (maxPrice == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
        };
    }

    public static Specification<Product> containsNameOrBrand(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isBlank()) {
                return null;
            }

            String pattern = search.trim().toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), pattern)
            );
        };
    }

}
