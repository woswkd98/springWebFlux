
package com.project.backend.routerEx;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST; // static 붙이는 이유가 함수형을 import 시키기 위해
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import org.springframework.web.reactive.function.server.RouterFunction;

import lombok.extern.java.Log;

import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static reactor.core.publisher.Mono.just;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.project.backend.Model.*;
import com.project.backend.Repository.UserRepository;
import com.project.backend.jwt.JwtProduct;

import org.springframework.util.MultiValueMap;
import java.text.ParseException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.gen.*;
import com.nimbusds.jwt.*;
import com.nimbusds.oauth2.sdk.id.Subject;
import com.nimbusds.jose.util.Base64;
import java.util.Date;
import com.project.backend.Configurations.*;

@Configuration
class UserController {
    @Autowired
    JwtProduct jwtProduct;

    private final UserRepository userRepository;

    private final DatabaseClient databaseClient;

    public UserController(DatabaseClient databaseClient, UserRepository userRepository) {
        this.databaseClient = databaseClient;
        this.userRepository = userRepository;
    }

    @Bean
    public RouterFunction<?> join() {
        return route(POST("/join"), req -> {
            Mono<User> insertPub = req.bodyToMono(User.class);
            return ok().body(insertPub.flatMap(u -> {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                u.setPassword(passwordEncoder.encode(u.getPassword()));
                Mono<Integer> rs = databaseClient
                        .execute("insert into User(id, password, name, email) values( :id, :password, :name, :email)")
                        .as(User.class).bind("id", u.getId()).bind("password", u.getPassword())
                        .bind("name", u.getName()).bind("email", u.getEmail()).fetch().rowsUpdated().onErrorReturn(0); // 해결봤다
                return rs;
            }), Integer.class);
        });
    }

    @Bean
    public RouterFunction<?> login() {
        return route(POST("/login"), req -> {

            Mono<LoginModel> mUser = req.bodyToMono(LoginModel.class);
            Mono<Integer> t = mUser.flatMap(u -> {
                return databaseClient.execute("select * from User where id = :id").bind("id", u.getId()).as(User.class)
                        .fetch().first().flatMap(nu -> {
                            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                            if (!passwordEncoder.matches(u.getPassword(), nu.getPassword())) {
                                return Mono.just(0);
                            }

                            try {
                                if (!jwtProduct.verify(nu.getId(), u.getToken())) {
                                    return Mono.just(0);
                                }
                            } catch (ParseException | JOSEException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            return Mono.just(1);
                        });
            });
            return ok().body(t.subscribe(), Integer.class);
        });
    }

    @Bean
    public RouterFunction<?> logout() {
        return route(GET("/logout"), req -> {
            return ok().body(Mono.just("Success"), String.class);
        });
    }


    
}