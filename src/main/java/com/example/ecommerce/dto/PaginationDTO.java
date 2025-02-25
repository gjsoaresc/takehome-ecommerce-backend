package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
public class PaginationDTO {
    private final int totalPages;
    private final long totalElements;
    private final int pageNumber;
    private final int pageSize;
    private final boolean isFirst;
    private final boolean isLast;
}
