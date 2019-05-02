package com.example.hellodbr2dbcclienth2;

import io.r2dbc.client.R2dbc;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

public class MessageHandler {

    private final R2dbc r2dbc;


    public MessageHandler(R2dbc r2dbc) {
        this.r2dbc = r2dbc;
    }

    public RouterFunction<ServerResponse> routes() {
        return route()
            .GET("/messages", this::getMessages)
            .POST("/messages", this::postMessage)
            .POST("/init", this::init)
            .build();
    }

    Mono<ServerResponse> getMessages(ServerRequest req) {
        final Flux<Message> messages = this.r2dbc.withHandle(handle -> handle
            .select("SELECT text FROM message ORDER BY created_at DESC")
            .mapResult(result -> result.map((row, meta) -> {
                final String text = row.get("text", String.class);
                return new Message(text);
            })));
        return ok().body(messages, Message.class);
    }

    Mono<ServerResponse> postMessage(ServerRequest req) {
        final Mono<Message> body = req.bodyToMono(Message.class)
            .delayUntil(message -> this.r2dbc
                .inTransaction(handle ->
                    handle
                        .execute("INSERT INTO message(text) VALUES($1)", message.getText())
                        .then()));
        return status(CREATED).body(body, Message.class);
    }

    Mono<ServerResponse> init(ServerRequest req) {
        return ok().body(this.r2dbc.withHandle(handle -> handle.execute("TRUNCATE TABLE message").then()), Void.class);
    }
}
