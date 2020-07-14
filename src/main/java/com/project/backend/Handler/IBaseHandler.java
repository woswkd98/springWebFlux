package com.project.backend.Handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import org.springframework.web.reactive.function.server.ServerResponse;
public interface IBaseHandler {
    public Mono<ServerResponse> getList(ServerRequest req);
    public Mono<ServerResponse> create(ServerRequest req);
    public Mono<ServerResponse> update(ServerRequest req);
    public Mono<ServerResponse> delete(ServerRequest req);
}