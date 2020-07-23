package com.project.backend.repositories;


import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.project.backend.Model.User;
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Query("SELECT * FROM User")
    Flux<User> getAll();

    @Query("select u from User u where u.id = :id and u.password : password")
    Flux<User> findByIdAndPwd(User user);

    @Query("select u from User u where u.id = :id")
    Flux<User> findById(String id);
}