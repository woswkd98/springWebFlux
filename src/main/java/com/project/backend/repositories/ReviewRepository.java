package com.project.backend.repositories;

import com.project.backend.Model.Review;

import org.springframework.data.keyvalue.repository.config.QueryCreatorType;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

public interface ReviewRepository extends ReactiveCrudRepository<Review,Long> {
    @Query(
        "insert into Review (grade, context, seller_sellerId, user_indexId)"+
        "values (:grade, :context, :sellerId, :userId)"
    )
    public Mono<Integer> insertReview(
        float grade,
        String context,
        String uploadAt,
        int sellerId,
        int userId
    );
  

}