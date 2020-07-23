package com.project.backend.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.project.backend.Model.*;

public interface RequestRepository extends ReactiveCrudRepository<User, Long> {
    



}