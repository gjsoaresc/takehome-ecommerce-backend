package com.example.ecommerce.controllers;

import com.example.ecommerce.dto.ProductFilterDTO;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "Endpoints for managing products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products", description = "Retrieves a list of all available products.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @Operation(summary = "Get product by ID", description = "Retrieves a product based on its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProductById(@Parameter(description = "Product ID", required = true) @PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("{\"error\": \"Product not found\"}");
        }
    }

    @Operation(summary = "Create a new product", description = "Adds a new product to the catalog.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @Operation(summary = "Filter products", description = "Filters products by category, brand, color, size, and price range with sorting and pagination.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filtered products retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Product>> filterProducts(
            @Parameter(description = "Category of the product") @RequestParam(required = false) String category,
            @Parameter(description = "Brand of the product") @RequestParam(required = false) String brand,
            @Parameter(description = "Color of the product") @RequestParam(required = false) String color,
            @Parameter(description = "Size of the product") @RequestParam(required = false) String size,
            @Parameter(description = "Minimum price of the product") @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Maximum price of the product") @RequestParam(required = false) Double maxPrice,
            @Parameter(description = "Field to sort by (default: price)") @RequestParam(defaultValue = "price") String sortBy,
            @Parameter(description = "Sort order (asc or desc, default: asc)") @RequestParam(defaultValue = "asc") String sortOrder,
            @Parameter(description = "Page number (default: 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Items per page (default: 10)") @RequestParam(defaultValue = "10") int sizePerPage) {

        ProductFilterDTO filterDTO = new ProductFilterDTO();
        filterDTO.setCategory(category);
        filterDTO.setBrand(brand);
        filterDTO.setColor(color);
        filterDTO.setSize(size);
        filterDTO.setMinPrice(minPrice);
        filterDTO.setMaxPrice(maxPrice);
        filterDTO.setSortBy(sortBy);
        filterDTO.setSortOrder(sortOrder);

        return ResponseEntity.ok(productService.getFilteredProducts(filterDTO, page, sizePerPage));
    }
}
