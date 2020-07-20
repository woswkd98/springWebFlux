package com.project.backend.handlers;

import org.springframework.stereotype.Component;
import com.project.backend.repositories.ProductRepository;

public class ProductHandler {
    private final ProductRepository products;

    public ProductHandler(ProductRepository products) {
        this.products = products;
    }

    
}