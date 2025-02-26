package com.example.ecommerce.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PaginatedResponseDTO<T> {
    private final List<T> content;
    private final PaginationDTO pagination;
    private final FiltersDTO filters;

    public PaginatedResponseDTO(Page<T> page, FiltersDTO filters) {
        PaginationDTO pagination = new PaginationDTO(
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast()
        );

        this.content = page.getContent();
        this.pagination = pagination;
        this.filters = filters;
    }
}
