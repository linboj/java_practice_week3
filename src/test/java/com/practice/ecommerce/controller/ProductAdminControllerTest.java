package com.practice.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.ecommerce.controller.admin.ProductAdminController;
import com.practice.ecommerce.dto.UpsertProductDTO;
import com.practice.ecommerce.entity.Product;
import com.practice.ecommerce.filter.JwtRequestFilter;
import com.practice.ecommerce.repository.UserRepository;
import com.practice.ecommerce.service.JwtService;
import com.practice.ecommerce.service.ProductService;
import com.practice.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductAdminController.class)
@Import({JwtRequestFilter.class, UserService.class, JwtService.class})
public class ProductAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"USER", "ADMIN"})
    public void createProduct() throws Exception {
        Product product = Product.builder()
                .id(null).name("TEST1").description("TEST1").price(BigDecimal.valueOf(10)).stock(1).build();
        Product savedProduct = Product.builder()
                .id(1L).name("TEST1").description("TEST1").price(BigDecimal.valueOf(10)).stock(1).build();

        when(productService.createProduct(any(UpsertProductDTO.class))).thenReturn(savedProduct);

        mockMvc.perform(post("/api/admin/products").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("TEST1"))
                .andExpect(jsonPath("$.data.description").value("TEST1"));

        verify(productService, times(1)).createProduct(any(UpsertProductDTO.class));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"USER", "ADMIN"})
    public void updateProduct() throws Exception {
        Product updatedProduct = Product.builder()
                .id(null).name("TEST1").description("TEST1").price(BigDecimal.valueOf(10)).stock(1).build();

        when(productService.updateProduct(eq(1L), any(UpsertProductDTO.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/admin/products/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("TEST1"))
                .andExpect(jsonPath("$.data.stock").value(1));

        verify(productService, times(1)).updateProduct(eq(1L), any(UpsertProductDTO.class));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"USER", "ADMIN"})
    public void deleteProduct() throws Exception {

        mockMvc.perform(delete("/api/admin/products/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(productService, times(1)).deleteProduct(1L);
    }
}
