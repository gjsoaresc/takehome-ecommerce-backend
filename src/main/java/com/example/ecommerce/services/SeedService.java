package com.example.ecommerce.services;

import com.example.ecommerce.models.Product;
import com.example.ecommerce.models.User;
import com.example.ecommerce.models.Role;
import com.example.ecommerce.repositories.ProductRepository;
import com.example.ecommerce.repositories.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeedService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final UnsplashService unsplashService;

    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void verifyAdminUser() {
        if (userRepository.findByRole(Role.ADMIN).isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setName("Admin User");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
            System.out.println("Admin user created: admin@example.com / admin123");
        } else {
            System.out.println("Admin user already exists.");
        }
    }

    public String seedDatabase() {
        if (productRepository.count() == 0) {
            try {
                InputStream inputStream = new ClassPathResource("products.json").getInputStream();
                List<Product> products = objectMapper.readValue(inputStream, new TypeReference<>() {});

                for (Product product : products) {
                    if (product.getImageUrl() == null || product.getImageUrl().isBlank()) {
                        product.setImageUrl(unsplashService.getShoeImageUrl(product.getColor().toLowerCase()));
                    }
                }

                productRepository.saveAll(products);

                return "Database seeding completed successfully!";
            } catch (IOException e) {
                return "Error reading products.json: " + e.getMessage();
            }
        }
        return "Database already seeded!";
    }
}
