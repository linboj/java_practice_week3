package com.practice.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.ecommerce.entity.Product;
import com.practice.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createProduct() throws Exception {
        Product product = Product.builder()
                .id(null).name("TEST1").description("TEST1").price(BigDecimal.valueOf(10)).stock(1).build();
        Product savedProduct = Product.builder()
                .id(1L).name("TEST1").description("TEST1").price(BigDecimal.valueOf(10)).stock(1).build();

        when(productService.createProduct(any(Product.class))).thenReturn(savedProduct);

        mockMvc.perform(post("/api/admin/products").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("TEST1"))
                .andExpect(jsonPath("$.data.description").value("TEST1"));

        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    public void getProduct() throws Exception {
        Product product = Product.builder()
                .id(1L).name("TEST1").description("TEST1").price(BigDecimal.valueOf(10)).stock(1).build();

        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("TEST1"))
                .andExpect(jsonPath("$.data.stock").value(1));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    public void updateProduct() throws Exception {
        Product updatedProduct = Product.builder()
                .id(null).name("TEST1").description("TEST1").price(BigDecimal.valueOf(10)).stock(1).build();

        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/admin/products/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("TEST1"))
                .andExpect(jsonPath("$.data.stock").value(1));

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    public void deleteProduct() throws Exception {

        mockMvc.perform(delete("/api/products/1")).andExpect(status().isOk());

        verify(productService, times(1)).deleteProduct(1L);
    }
}
