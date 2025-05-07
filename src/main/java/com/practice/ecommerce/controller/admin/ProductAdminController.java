package com.practice.ecommerce.controller.admin;

import com.practice.ecommerce.dto.UnifiedAPIResponse;
import com.practice.ecommerce.dto.UpsertProductDTO;
import com.practice.ecommerce.entity.Product;
import com.practice.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
public class ProductAdminController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @Operation(
            summary = "Create a new product.",
            description = "Add a new product.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product Created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UnifiedAPIResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
            }
    )
    public ResponseEntity<UnifiedAPIResponse<Product>> create(@Valid @RequestBody UpsertProductDTO product) {
        Product newProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UnifiedAPIResponse<>(true, newProduct, null));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing product",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product updated"),
                    @ApiResponse(responseCode = "404", description = "Product not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public ResponseEntity<UnifiedAPIResponse<Product>> update(@PathVariable Long id, @Valid @RequestBody UpsertProductDTO product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(new UnifiedAPIResponse<>(true, updatedProduct, null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product deleted"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            })
    public ResponseEntity<UnifiedAPIResponse<Void>> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new UnifiedAPIResponse<>(true, null, null));
    }
}
