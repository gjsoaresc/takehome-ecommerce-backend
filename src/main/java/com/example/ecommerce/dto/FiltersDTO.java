package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FiltersDTO {
    private final List<String> categories;
    private final List<String> brands;
    private final List<String> colors;
    private final List<String> sizes;
    private final Integer maxPrice;
}
