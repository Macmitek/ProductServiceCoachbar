package com.example.productmanagementservice.controller;

import com.example.productmanagementservice.model.Product;
import com.example.productmanagementservice.service.ProductService;
import com.example.productmanagementservice.service.TokenValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/products/")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private TokenValidationService tokenValidationService;


    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        return productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @PostMapping
    public Product createProduct(@RequestHeader("Authorization") String authHeader,
                                 @RequestBody Product product) {
        System.out.println("inside create product");
        List<String> roles = tokenValidationService.validateTokenAndGetRoles(authHeader.replace("Bearer ", ""));
        System.out.println("roles ::"+roles);

        if (!roles.contains("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only ADMINs can create products");
        }
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@RequestHeader("Authorization") String authHeader,
                                 @PathVariable String id, @RequestBody Product product) {
        // Validate token and roles for the user
        List<String> roles = tokenValidationService.validateTokenAndGetRoles(authHeader.replace("Bearer ", ""));
        if (!roles.contains("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only ADMINs can update products");
        }

        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@RequestHeader("Authorization") String authHeader, @PathVariable String id) {
        // Validate token and roles for the user
        List<String> roles = tokenValidationService.validateTokenAndGetRoles(authHeader.replace("Bearer ", ""));
        if (!roles.contains("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only ADMINs can delete products");
        }

        productService.deleteProduct(id);
    }
}
