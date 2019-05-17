package com.example.hellodbr2dbcdatabaseclienth2;

import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

public class MessageHandler {

    private final DatabaseClient databaseClient;

    private final TransactionalOperator rxtx;

    public MessageHandler(DatabaseClient databaseClient, TransactionalOperator rxtx) {
        this.databaseClient = databaseClient;
        this.rxtx = rxtx;
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
        // or
//        final Flux<Message> messages = this.databaseClient
//            .execute()
//            .sql("SELECT text FROM message ORDER BY created_at DESC")
//            .as(Message.class)
//            .fetch()
//            .all();
        return ok().body(messages, Message.class);
    }

    Mono<ServerResponse> postMessage(ServerRequest req) {
        final Mono<Message> body = req.bodyToMono(Message.class)
            .delayUntil(message -> this.rxtx.execute(status ->
                this.databaseClient.insert()
                    .into(Message.class)
                    .using(message)
                    .fetch()
                    .rowsUpdated()));
//        final Mono<Message> body = req.bodyToMono(Message.class)
//            .delayUntil(message -> this.rxtx.execute(status ->
//                this.databaseClient.execute()
//                  .sql("INSERT INTO message(text) VALUES($1)")
//                  .bind("$1", message.getText())
//                  .fetch()
//                  .rowsUpdated()));
        return status(CREATED).body(body, Message.class);
    }

    Mono<ServerResponse> init(ServerRequest req) {
        return ok().body(this.databaseClient.execute().sql("TRUNCATE TABLE message").then(), Void.class);
    }
}
