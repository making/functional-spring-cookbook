package com.example.hellovalidationbv;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-fn-handler-validation
 */
public class MessageHandler {

    private final List<Message> messages = new CopyOnWriteArrayList<>();

    private final Validator validator;

    public MessageHandler(Validator validator) {
        this.validator = validator;
    }

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
        Mono<Message> body = req.bodyToMono(Message.class)
            .doOnNext(message -> {
                BindingResult bindingResult = new BeanPropertyBindingResult(message, "message");
                validator.validate(message, bindingResult);
                if (bindingResult.hasErrors()) {
                    throw new ServerWebInputException(null);
                }
            })
            .doOnNext(messages::add);
        return ok().body(body, Message.class);
    }
}
