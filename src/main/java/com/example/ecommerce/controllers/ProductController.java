package com.example.ecommerce.controllers;

import com.example.ecommerce.dto.FiltersDTO;
import com.example.ecommerce.dto.PaginatedResponseDTO;
import com.example.ecommerce.dto.ProductFilterDTO;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "Endpoints for managing products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products", description = "Retrieves a paginated list of all available products.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Product>> getAllProducts(
            @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Items per page (default: 10)") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field (default: price)") @RequestParam(defaultValue = "price") String sortBy,
            @Parameter(description = "Sort order (asc or desc, default: asc)") @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        Page<Product> products = productService.getAllProducts(page, size, sortBy, sortOrder);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get product by ID", description = "Retrieves a product based on its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProductById(@Parameter(description = "Product ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Create a new product", description = "Adds a new product to the catalog.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @Operation(summary = "Update an existing product", description = "Modifies an existing product by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "Product ID", required = true) @PathVariable Long id,
            @Valid @RequestBody Product productDetails
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, productDetails));
    }

    @Operation(summary = "Filter products with pagination", description = "Returns paginated and filtered product results.")
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaginatedResponseDTO<Product>> filterProducts(
            @Parameter(description = "Categories of the product (comma-separated)") @RequestParam(required = false) String categories,
            @Parameter(description = "Brands of the product (comma-separated)") @RequestParam(required = false) String brands,
            @Parameter(description = "Colors of the product (comma-separated)") @RequestParam(required = false) String colors,
            @Parameter(description = "Shoe sizes of the product (comma-separated)") @RequestParam(required = false) String shoeSizes,
            @Parameter(description = "Minimum price of the product") @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Maximum price of the product") @RequestParam(required = false) Double maxPrice,
            @Parameter(description = "Field to sort by (default: price)") @RequestParam(defaultValue = "price") String sortBy,
            @Parameter(description = "Sort order (asc or desc, default: asc)") @RequestParam(defaultValue = "asc") String sortOrder,
            @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Items per page (default: 10)") @RequestParam(defaultValue = "10") int sizePerPage
    ) {
        ProductFilterDTO filterDTO = new ProductFilterDTO();

        filterDTO.setCategories(categories != null ? Arrays.asList(categories.split(",")) : List.of());
        filterDTO.setBrands(brands != null ? Arrays.asList(brands.split(",")) : List.of());
        filterDTO.setColors(colors != null ? Arrays.asList(colors.split(",")) : List.of());
        filterDTO.setShoeSizes(shoeSizes != null ? Arrays.asList(shoeSizes.split(",")) : List.of());
        filterDTO.setMinPrice(minPrice);
        filterDTO.setMaxPrice(maxPrice);
        filterDTO.setSortBy(sortBy);
        filterDTO.setSortOrder(sortOrder);

        FiltersDTO filters = productService.getProductFilterInfo();

        Page<Product> filteredProducts = productService.getFilteredProducts(filterDTO, page, sizePerPage);
        return ResponseEntity.ok(new PaginatedResponseDTO<>(filteredProducts, filters));
    }


    @Operation(summary = "Delete a product", description = "Deletes a product by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProduct(@Parameter(description = "Product ID", required = true) @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("{\"message\": \"Product deleted successfully\"}");
    }
}
