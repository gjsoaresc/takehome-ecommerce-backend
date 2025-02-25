package com.example.ecommerce.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Min(value = 0, message = "Price must be a positive value")
    private double price;

    private String description;

    @NotBlank(message = "Category cannot be empty")
    private String category;

    @NotBlank(message = "Brand cannot be empty")
    private String brand;

    @NotBlank(message = "Color cannot be empty")
    private String color;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "size")
    private List<String> sizes;

    private String imageUrl;
}
