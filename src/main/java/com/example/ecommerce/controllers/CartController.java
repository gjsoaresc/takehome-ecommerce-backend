package com.example.ecommerce.controllers;

import com.example.ecommerce.models.Cart;
import com.example.ecommerce.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart API", description = "Manage user shopping carts")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Get cart by user ID", description = "Retrieves the shopping cart for a specific user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> getCart(@Parameter(description = "User ID", required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @Operation(summary = "Add product to cart", description = "Adds a product to the user's shopping cart.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product added to cart"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping(value = "/{userId}/add/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cart> addToCart(
            @Parameter(description = "User ID", required = true) @PathVariable Long userId,
            @Parameter(description = "Product ID", required = true) @PathVariable Long productId,
            @Parameter(description = "Quantity of the product", required = true) @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    @Operation(summary = "Remove item from cart", description = "Removes a specific item from the user's cart.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item removed successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found in cart")
    })
    @DeleteMapping(value = "/{userId}/remove/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeFromCart(
            @Parameter(description = "User ID", required = true) @PathVariable Long userId,
            @Parameter(description = "Cart Item ID", required = true) @PathVariable Long itemId) {
        cartService.removeFromCart(userId, itemId);
        return ResponseEntity.ok("{\"message\": \"Item removed successfully\"}");
    }

    @Operation(summary = "Get total cart price", description = "Calculates the total price of items in the user's cart.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Total price retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Cart is empty")
    })
    @GetMapping(value = "/{userId}/total", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTotalPrice(@Parameter(description = "User ID", required = true) @PathVariable Long userId) {
        double totalPrice = cartService.getTotalPrice(userId);
        return ResponseEntity.ok("{\"total\": " + totalPrice + "}");
    }

    @Operation(summary = "Checkout cart", description = "Processes the checkout of a user's cart.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Checkout successful"),
            @ApiResponse(responseCode = "400", description = "Cart is empty")
    })
    @PostMapping(value = "/{userId}/checkout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkout(@Parameter(description = "User ID", required = true) @PathVariable Long userId) {
        cartService.checkout(userId);
        return ResponseEntity.ok("{\"message\": \"Checkout successful!\"}");
    }

    @Operation(summary = "Delete cart", description = "Deletes the entire cart of a user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteCart(@Parameter(description = "User ID", required = true) @PathVariable Long userId) {
        cartService.deleteCart(userId);
        return ResponseEntity.ok("{\"message\": \"Cart deleted successfully\"}");
    }
}
