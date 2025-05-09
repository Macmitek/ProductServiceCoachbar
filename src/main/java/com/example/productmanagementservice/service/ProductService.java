package com.example.productmanagementservice.service;

import com.example.productmanagementservice.model.Product;
import com.example.productmanagementservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(String id, Product product) {
        // First, check if the product exists
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        // Update the product with the new values
        Product updatedProduct = existingProduct.get();
        updatedProduct.setName(product.getName()); // Update product name
        updatedProduct.setDescription(product.getDescription()); // Update description
        updatedProduct.setPrice(product.getPrice()); // Update price
        updatedProduct.setQuantity(product.getQuantity()); // Update quantity

        return productRepository.save(updatedProduct); // Save and return updated product
    }

    public void deleteProduct(String id) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        productRepository.deleteById(id); // Delete product
    }
}
