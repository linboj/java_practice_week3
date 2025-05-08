package com.practice.ecommerce.service;

import com.practice.ecommerce.dto.UpsertProductDTO;
import com.practice.ecommerce.entity.Product;
import com.practice.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void testCreateProduct() {

        // arrange
        Product product = new Product();
        product.setName("TEST1");
        product.setPrice(BigDecimal.valueOf(10));
        product.setStock(2);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        // act
        UpsertProductDTO upsertProductDTO = new UpsertProductDTO("TEST1", "", BigDecimal.valueOf(10), 2);
        Product savedProduct = productService.createProduct(upsertProductDTO);

        // assert
        assertNotNull(savedProduct);
        assertEquals("TEST1", savedProduct.getName());
        assertEquals(BigDecimal.valueOf(10), savedProduct.getPrice());
        assertNotEquals(3, savedProduct.getStock());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {

        // arrange
        Long id = 1L;
        Product originalProduct = new Product();
        originalProduct.setId(id);
        originalProduct.setName("TEST1");
        originalProduct.setPrice(BigDecimal.valueOf(10));
        originalProduct.setStock(2);

        Product updatedProduct = new Product();
        updatedProduct.setId(id);
        updatedProduct.setName("TEST1");
        updatedProduct.setPrice(BigDecimal.valueOf(100));
        updatedProduct.setStock(20);

        when(productRepository.findById(id)).thenReturn(Optional.of(originalProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // act
        UpsertProductDTO upsertProductDTO = new UpsertProductDTO("TEST1", "", BigDecimal.valueOf(100), 20);
        Product savedProduct = productService.updateProduct(id, upsertProductDTO);

        // assert
        assertNotNull(savedProduct);
        assertEquals("TEST1", savedProduct.getName());
        assertEquals(BigDecimal.valueOf(100), savedProduct.getPrice());
        assertNotEquals(2, savedProduct.getStock());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void testGetProductById() {

        // arrange
        Long id = 1L;
        Product product = new Product();
        product.setId(id);
        product.setName("TEST2");

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // act
        Product foundProduct = productService.getProductById(1L);

        // assert
        assertNotNull(foundProduct);
        assertEquals("TEST2", foundProduct.getName());
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteProduct() {

        // arrange
        Long id = 1L;

//        when(productRepository.existsById(id)).thenReturn(true);
        doNothing().when(productRepository).deleteById(id);

        // act
        productService.deleteProduct(1L);

        // assert
//        verify(productRepository, times(1)).existsById(id);
        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    void getAllProducts_ShouldReturnPagedResultsInAscendingOrder() {

        // arrange
        int page = 0;
        int size = 10;
        String sortBy = "id";
        Sort.Direction sortDirection = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        List<Product> products = Arrays.asList(
                Product.builder().id(1L).name("TEST1").description("TEST1").price(BigDecimal.valueOf(10)).stock(1).build(),
                Product.builder().id(2L).name("TEST2").description("TEST2").price(BigDecimal.valueOf(1.0)).stock(3).build(),
                Product.builder().id(3L).name("TEST3").description("TEST3").price(BigDecimal.valueOf(2.5)).stock(2).build()
        );
        Page<Product> expectedPage = new PageImpl<>(products);

        when(productRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        // act
        Page<Product> result = productService.getAllProducts(pageable);

        // assert
        assertEquals(expectedPage, result);
        assertEquals(3, result.getContent().size());
        assertEquals("TEST1", result.getContent().get(0).getName());
        assertEquals("TEST3", result.getContent().get(2).getName());
    }

    @Test
    void getAllProducts_ShouldReturnPagedResultsInDescendingOrder() {

        // arrange
        int page = 1;
        int size = 5;
        String sortBy = "name";
        Sort.Direction sortDirection = Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        List<Product> products = Arrays.asList(
                Product.builder().id(3L).name("TEST3").description("TEST3").price(BigDecimal.valueOf(2.5)).stock(2).build(),
                Product.builder().id(2L).name("TEST2").description("TEST2").price(BigDecimal.valueOf(1.0)).stock(3).build(),
                Product.builder().id(1L).name("TEST1").description("TEST1").price(BigDecimal.valueOf(10)).stock(1).build()
        );
        Page<Product> expectedPage = new PageImpl<>(products);

        when(productRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        // act
        Page<Product> result = productService.getAllProducts(pageable);

        // assert
        assertEquals(expectedPage, result);
        assertEquals(3, result.getContent().size());
        assertEquals("TEST3", result.getContent().get(0).getName());
        assertEquals("TEST1", result.getContent().get(2).getName());
    }
}
