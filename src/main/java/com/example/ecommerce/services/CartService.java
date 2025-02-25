package com.example.ecommerce.services;

import com.example.ecommerce.exceptions.BadRequestException;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.models.Cart;
import com.example.ecommerce.models.CartItem;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.repositories.CartRepository;
import com.example.ecommerce.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public Cart addToCart(Long userId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0.");
        }

        Cart cart = getCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId));

        CartItem item = new CartItem(null, cart, product, quantity);
        cart.getItems().add(item);
        cartRepository.save(cart);

        return cart;
    }

    @Transactional
    public void removeFromCart(Long userId, Long itemId) {
        Cart cart = getCartByUserId(userId);
        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(itemId));

        if (!removed) {
            throw new ResourceNotFoundException("Cart Item", "ID", itemId);
        }

        cartRepository.save(cart);
    }

    public double getTotalPrice(Long userId) {
        Cart cart = getCartByUserId(userId);
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty. Cannot calculate total price.");
        }

        return cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    @Transactional
    public void checkout(Long userId) {
        Cart cart = getCartByUserId(userId);
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty. Add items before checkout.");
        }

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Transactional
    public void deleteCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "User ID", userId));

        cartRepository.delete(cart);
    }
}
