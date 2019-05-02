package com.example.hellodbr2dbcdatabaseclienth2;

import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.function.TransactionalDatabaseClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

public class MessageHandler {

    private final List<Message> messages = new CopyOnWriteArrayList<>();

    private final TransactionalDatabaseClient databaseClient;

    public MessageHandler(TransactionalDatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public RouterFunction<ServerResponse> routes() {
        return route()
            .GET("/messages", this::getMessages)
            .POST("/messages", this::postMessage)
            .POST("/init", this::init)
            .build();
    }

    Mono<ServerResponse> getMessages(ServerRequest req) {
        final Flux<Message> messages = this.databaseClient
            .select()
            .from(Message.class)
            .orderBy(Sort.by(desc("created_at")))
            .as(Message.class)
            .all();
        return ok().body(messages, Message.class);
    }

    Mono<ServerResponse> postMessage(ServerRequest req) {
        Mono<Integer> updated = req.bodyToMono(Message.class)
            .flatMap(message -> this.databaseClient
                .inTransaction(client ->
                    client.insert()
                        .into(Message.class)
                        .using(message)
                        .fetch()
                        .rowsUpdated())
                .single());
        return status(CREATED).body(updated.then(), Void.class);
    }

    Mono<ServerResponse> init(ServerRequest req) {
        return ok().body(this.databaseClient.execute().sql("TRUNCATE TABLE message").then(), Void.class);
    }
}
