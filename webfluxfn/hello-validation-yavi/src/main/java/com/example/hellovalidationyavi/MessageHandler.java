package com.example.hellovalidationyavi;

import am.ik.yavi.core.ConstraintViolations;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

public class MessageHandler {

    private final List<Message> messages = new CopyOnWriteArrayList<>();

    public RouterFunction<ServerResponse> routes() {
        return route()
            .GET("/messages", this::getMessages)
            .POST("/messages", this::postMessage)
            .build();
    }

    Mono<ServerResponse> getMessages(ServerRequest req) {
        return ok().syncBody(this.messages);
    }

    Mono<ServerResponse> postMessage(ServerRequest req) {
        return req.bodyToMono(Message.class)
            .flatMap(b -> Message.validator.validateToEither(b)
                .bimap(ConstraintViolations::details, this.messages::add)
                .fold(v -> badRequest().syncBody(v), body -> ok().syncBody(b)));
    }
}
