package com.practice.ecommerce.controller;

import com.practice.ecommerce.dto.UnifiedAPIResponse;
import com.practice.ecommerce.entity.Product;
import com.practice.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/public/products")
@Tag(name = "Product", description = "產品公開查詢 API")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @Operation(
            summary = "Get all products.",
            description = "Retrieve paginated list of all products.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of products",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UnifiedAPIResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<UnifiedAPIResponse<Page<Product>>> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(new UnifiedAPIResponse<>(true, products, null));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get product by ID.",
            description = "Fetch a single product by its unique ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UnifiedAPIResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found"),
            }
    )
    public ResponseEntity<UnifiedAPIResponse<Product>> getById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(new UnifiedAPIResponse<>(true, product, null));
    }
}
