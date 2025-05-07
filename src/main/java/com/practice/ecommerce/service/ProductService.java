package com.practice.ecommerce.service;

import com.practice.ecommerce.dto.UpsertProductDTO;
import com.practice.ecommerce.entity.Product;
import com.practice.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    };

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public Product createProduct(UpsertProductDTO product) {
        Product newProduct = new Product();
        dtoMappingToEntity(newProduct, product);
        return productRepository.save(newProduct);
    }

    public Product updateProduct(Long id, UpsertProductDTO updatedProduct) {
        Product existing = getProductById(id);
        dtoMappingToEntity(existing, updatedProduct);
        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private void dtoMappingToEntity (Product entity, UpsertProductDTO dto){
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());
    }
}
