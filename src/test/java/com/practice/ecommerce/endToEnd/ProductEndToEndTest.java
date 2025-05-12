package com.practice.ecommerce.endToEnd;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.ecommerce.RestPageImpl;
import com.practice.ecommerce.dto.AuthRequest;
import com.practice.ecommerce.dto.UnifiedAPIResponse;
import com.practice.ecommerce.dto.UpsertProductDTO;
import com.practice.ecommerce.entity.Product;
import com.practice.ecommerce.repository.ProductRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String username = "admin@test.com";
    private final String password = "adminTEST";

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        String token = getJwtToken();
        System.out.println("HERE IS TOKEN " + token);
        RestAssured.requestSpecification = given()
                .header(new Header("Authorization", "Bearer " + token));
    }

    private String getJwtToken() {

        return given()
                .contentType(ContentType.JSON)
                .body(new AuthRequest(username, password))
                .when()
                .post("http://localhost:" + port + "/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @Test
    void getAllProducts() {

        // add test records
        Product product1 = new Product(null, "Test1", "Test for get all 1", BigDecimal.valueOf(2.4), 10);
        Product product2 = new Product(null, "Test2", "Test for get all 2", BigDecimal.valueOf(10.0), 1);
        productRepository.saveAll(Arrays.asList(product1, product2));

        // call API
        var response = restTemplate.getForEntity("/api/public/products", UnifiedAPIResponse.class);

        // validate response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        RestPageImpl<Product> pagedProducts = objectMapper.convertValue(response.getBody().getData(), new TypeReference<>() {
        });
        List<Product> products = objectMapper.convertValue(pagedProducts.getContent(), new TypeReference<>() {
        });

        // validate response data
        assertEquals(2, products.size());
        assertEquals(product1.getName(), products.get(0).getName());
        assertEquals(product1.getStock(), products.get(0).getStock());
        assertEquals(product2.getName(), products.get(1).getName());
        assertEquals(product2.getPrice(), products.get(1).getPrice());
    }

    @Test
    void getProduct() {

        // create test record
        Product product = productRepository.save(new Product(null, "Test1", "Test for get 1", BigDecimal.valueOf(2.4), 10));

        // call api
        var response = restTemplate.getForEntity("/api/public/products/" + product.getId(), UnifiedAPIResponse.class);

        // validate response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());

        // validate response data
        Product responseProduct = objectMapper.convertValue(response.getBody().getData(), new TypeReference<>() {
        });
        assertEquals("Test for get 1", responseProduct.getDescription());
        assertEquals(10, responseProduct.getStock());
    }

    @Test
    void createProduct() {

        String url = "http://localhost:" + port + "/api/admin/products";
        UpsertProductDTO newProduct = new UpsertProductDTO("Test1", "Test for create", BigDecimal.valueOf(10), 10);

        given()
                .contentType(ContentType.JSON)
                .body(newProduct)
                .when()
                .post(url)
                .then()
                .statusCode(201)
                .body("success", equalTo(true))
                .body("data.description", equalTo(newProduct.getDescription()))
                .body("data.price", equalTo(10));

        // validate DB record
        List<Product> products = productRepository.findAll();
        assertEquals(1, products.size());
        assertEquals(newProduct.getDescription(), products.get(0).getDescription());
        assertEquals(newProduct.getStock(), products.get(0).getStock());
    }

    @Test
    void updateProduct() {

        // add test record
        Product product = productRepository.save(new Product(null, "Test1", "Test for update 1", BigDecimal.valueOf(2.4), 10));
        UpsertProductDTO updatedProduct = new UpsertProductDTO("Test1", "Test for update 2", BigDecimal.valueOf(4.2), 10);

        String url = "http://localhost:" + port + "/api/admin/products/" + product.getId();
        given()
                .contentType(ContentType.JSON)
                .body(updatedProduct)
                .when()
                .put(url)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data.description", not(product.getDescription()))
                .body("data.price", equalTo(4.2F));

        // validate DB record
        Product actualProduct = productRepository.findById(product.getId()).get();
        assertEquals(updatedProduct.getDescription(), actualProduct.getDescription());
        assertNotEquals(product.getPrice(), actualProduct.getPrice());
        assertEquals(updatedProduct.getStock(), actualProduct.getStock());
    }

    @Test
    void testDeleteProduct() {

        // add test record
        Product savedProduct = productRepository.save(new Product(null, "Test1", "Test for delete", BigDecimal.valueOf(2.4), 10));

        // call API
        String url = "http://localhost:" + port + "/api/admin/products/" + savedProduct.getId();

        given()
                .when()
                .delete(url)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));

        // validate BD record
        List<Product> products = productRepository.findAll();
        assertEquals(0, products.size());
    }
}
