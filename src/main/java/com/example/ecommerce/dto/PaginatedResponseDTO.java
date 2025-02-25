package com.example.ecommerce.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PaginatedResponseDTO<T> {
    private final List<T> content;
    private final PaginationDTO pagination;

    public PaginatedResponseDTO(Page<T> page) {
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
    }
}
