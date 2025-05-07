package com.practice.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name cannot be blank.")
    private String name;
    private String description;
    @DecimalMin(value = "0", message = "Product price cannot be less than 0.")
    private BigDecimal price;
    @PositiveOrZero(message = "Product stock cannot be less than 0")
    private Integer stock;
}
