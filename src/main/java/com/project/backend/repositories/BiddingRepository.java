package com.project.backend.repositories;

import com.project.backend.Model.Bidding;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BiddingRepository extends ReactiveCrudRepository<Bidding, Long> {
    @Query("delete from bidding where request_requestId = :id")
    public Mono<Integer> 
}