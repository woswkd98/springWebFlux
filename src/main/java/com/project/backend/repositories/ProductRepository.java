package com.project.backend.repositories;
import com.project.backend.Model.*;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
    
};


