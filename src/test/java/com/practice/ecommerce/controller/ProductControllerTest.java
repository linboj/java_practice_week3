package com.practice.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.ecommerce.entity.Product;
import com.practice.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void getAllProduct() throws Exception {
        List<Product> products = Arrays.asList(
                Product.builder().id(3L).name("TEST3").description("TEST3").price(BigDecimal.valueOf(2.5)).stock(2).build(),
                Product.builder().id(2L).name("TEST2").description("TEST2").price(BigDecimal.valueOf(1.0)).stock(3).build(),
                Product.builder().id(1L).name("TEST1").description("TEST1").price(BigDecimal.valueOf(10)).stock(1).build()
        );
        Page<Product> expectedPage = new PageImpl<>(products);
        int page = 1;
        int size = 5;
        String sortBy = "name";
        Sort.Direction sortDirection = Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        when(productService.getAllProducts(pageable)).thenReturn(expectedPage);

        mockMvc.perform(get("/api/products/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(3))
                .andExpect(jsonPath("$.data[0].name").value("TEST3"))
                .andExpect(jsonPath("$.data[0].stock").value(2))
                .andExpect(jsonPath("$.data[2].id").value(1))
                .andExpect(jsonPath("$.data[2].name").value("TEST1"))
                .andExpect(jsonPath("$.data[2].stock").value(1));

        verify(productService, times(1)).getAllProducts(pageable);
    }
}
