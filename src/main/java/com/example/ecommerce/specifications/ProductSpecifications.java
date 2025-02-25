package com.example.ecommerce.specifications;

import com.example.ecommerce.models.Product;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class ProductSpecifications {

    public static Specification<Product> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
                category == null ? null : criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<Product> hasBrand(String brand) {
        return (root, query, criteriaBuilder) ->
                brand == null ? null : criteriaBuilder.equal(root.get("brand"), brand);
    }

    public static Specification<Product> hasColor(String color) {
        return (root, query, criteriaBuilder) ->
                color == null ? null : criteriaBuilder.equal(root.get("color"), color);
    }

    public static Specification<Product> priceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) return null;
            if (minPrice == null) return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            if (maxPrice == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
        };
    }

    public static Specification<Product> hasSize(String size) {
        return (root, query, criteriaBuilder) -> {
            if (size == null) return null;
            Join<Object, Object> sizesJoin = root.join("sizes", JoinType.INNER);
            return criteriaBuilder.equal(sizesJoin, size);
        };
    }
}
