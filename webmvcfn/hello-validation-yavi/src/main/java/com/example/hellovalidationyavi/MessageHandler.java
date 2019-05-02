package com.example.hellovalidationyavi;

import am.ik.yavi.core.ConstraintViolations;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.badRequest;
import static org.springframework.web.servlet.function.ServerResponse.ok;

public class MessageHandler {

    private final List<Message> messages = new CopyOnWriteArrayList<>();

    public RouterFunction<ServerResponse> routes() {
        return route()
            .GET("/messages", this::getMessages)
            .POST("/messages", this::postMessage)
            .build();
    }

    ServerResponse getMessages(ServerRequest req) {
        return ok().body(this.messages);
    }

    ServerResponse postMessage(ServerRequest req) throws ServletException, IOException {
        Message message = req.body(Message.class);
        return Message.validator.validateToEither(message)
            .bimap(ConstraintViolations::details, this.messages::add)
            .fold(v -> badRequest().body(v), body -> ok().body(message));
    }
}
