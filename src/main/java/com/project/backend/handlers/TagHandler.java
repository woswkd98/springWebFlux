package com.project.backend.handlers;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import org.springframework.data.r2dbc.core.DatabaseClient;

import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;

import com.project.backend.Configurations.GetTimeZone;
import com.project.backend.Model.*;
import com.project.backend.repositories.TagRepository;

import java.util.Map;
import java.util.HashMap;
import java.util.List;




@Component
public class TagHandler {
    private final TagRepository tagRepository;
    private final DatabaseClient databaseClient;
    
    public TagHandler(DatabaseClient databaseClient,
    TagRepository tagRepository) {
        this.databaseClient = databaseClient;
        this.tagRepository = tagRepository;
    }
    /*
    public Flux<Request>  getRequestsByTagContext(ServerRequest req) {
      

    }*/
}