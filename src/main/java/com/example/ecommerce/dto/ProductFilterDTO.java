package com.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductFilterDTO {
    private List<String> categories;
    private List<String> brands;
    private List<String> colors;
    private List<String> shoeSizes;
    private Double minPrice;
    private Double maxPrice;
    private String sortBy = "price";
    private String sortOrder = "asc";
}
