package com.example.ecommerce.repositories;

import com.example.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query(value = """
        SELECT
            STRING_AGG(DISTINCT p.category, ',') AS categories,
            STRING_AGG(DISTINCT p.brand, ',') AS brands,
            STRING_AGG(DISTINCT p.color, ',') AS colors,
            STRING_AGG(DISTINCT ps.size, ',') AS sizes,
            MAX(p.price) AS maxPrice
        FROM products p
        LEFT JOIN product_sizes ps ON p.id = ps.product_id
    """, nativeQuery = true)
    Object getFilterInfoNative();
}
