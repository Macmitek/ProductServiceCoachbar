package com.example.productmanagementservice.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class TokenValidationService {


    private final RestTemplate restTemplate = new RestTemplate();
    private final String VALIDATION_URL = "http://localhost:8082/api/users/validate";

    public List<String> validateTokenAndGetRoles(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<String>> response = restTemplate.exchange(
                    VALIDATION_URL,
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<List<String>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Invalid or expired token");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Token validation failed: " + ex.getMessage());
        }
    }
}
