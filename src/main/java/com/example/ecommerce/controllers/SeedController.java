package com.example.ecommerce.controllers;

import com.example.ecommerce.services.SeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seed")
public class SeedController {

    private final SeedService seedService;

    public SeedController(SeedService seedService) {
        this.seedService = seedService;
    }

    @PostMapping
    public ResponseEntity<String> seedDatabase() {
        String response = seedService.seedDatabase();
        return ResponseEntity.ok(response);
    }
}
