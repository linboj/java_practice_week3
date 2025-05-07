package com.practice.ecommerce.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class UpsertProductDTO {
    @NotBlank(message = "Product name cannot be blank.")
    private String name;

    private String description;

    @DecimalMin(value = "0", message = "Product price cannot be less than 0.")
    private BigDecimal price;

    @PositiveOrZero(message = "Product stock cannot be less than 0")
    private Integer stock;
}
