package com.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilterDTO {
    private String category;
    private String brand;
    private String color;
    private String shoeSize;
    private Double minPrice;
    private Double maxPrice;
    private String sortBy = "price";
    private String sortOrder = "asc";
}
