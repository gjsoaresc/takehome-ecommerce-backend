package com.example.ecommerce.controllers;

import com.example.ecommerce.models.Cart;
import com.example.ecommerce.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<Cart> addToCart(@PathVariable Long userId, @PathVariable Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    @DeleteMapping("/{userId}/remove/{itemId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long userId, @PathVariable Long itemId) {
        cartService.removeFromCart(userId, itemId);
        return ResponseEntity.ok("✅ Item removed from cart");
    }

    @GetMapping("/{userId}/total")
    public ResponseEntity<Double> getTotalPrice(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getTotalPrice(userId));
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<String> checkout(@PathVariable Long userId) {
        cartService.checkout(userId);
        return ResponseEntity.ok("✅ Checkout successful!");
    }
}
