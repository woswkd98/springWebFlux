package com.project.backend.handlers;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import org.springframework.web.reactive.function.server.ServerResponse;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import org.springframework.data.r2dbc.core.DatabaseClient;

import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;


import com.project.backend.Configurations.GetTimeZone;
import com.project.backend.Model.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Map;
import java.util.HashMap;
@Component
public class UserHandler {

    private final DatabaseClient databaseClient;
    
    public UserHandler(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public Mono<ServerResponse> join(ServerRequest req) {
        Mono<User> insertPub = req.bodyToMono(User.class);
        return ServerResponse.ok().body(insertPub.flatMap(u -> {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            u.setUserPassword(passwordEncoder.encode(u.getUserPassword()));
            Mono<Integer> rs = databaseClient
                    .execute("insert into User(userId, userPassword, userName, userEmail) values( :id, :password, :name, :email)")
                    .as(User.class)
                    .bind("id", u.getUserId())
                    .bind("password", u.getUserPassword())
                    .bind("name", u.getUserName())
                    .bind("email", u.getUserEmail()).fetch().rowsUpdated().onErrorReturn(0); // 해결봤다
            return rs;
        }), Integer.class);
    }

    
}