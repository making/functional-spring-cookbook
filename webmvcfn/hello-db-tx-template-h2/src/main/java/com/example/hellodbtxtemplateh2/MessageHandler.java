package com.example.hellodbtxtemplateh2;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;
import static org.springframework.web.servlet.function.ServerResponse.status;

public class MessageHandler {

    private final JdbcTemplate jdbcTemplate;

    private final TransactionTemplate tx;

    public MessageHandler(JdbcTemplate jdbcTemplate, TransactionTemplate tx) {
        this.jdbcTemplate = jdbcTemplate;
        this.tx = tx;
    }

    public RouterFunction<ServerResponse> routes() {
        return route()
            .GET("/messages", this::getMessages)
            .POST("/messages", this::postMessage)
            .POST("/init", this::init)
            .build();
    }

    ServerResponse getMessages(ServerRequest req) {
        final List<Message> messages = this.jdbcTemplate
            .queryForList("SELECT text FROM message ORDER BY created_at DESC", Message.class);
        return ok().body(messages);
    }

    ServerResponse postMessage(ServerRequest req) throws ServletException, IOException {
        final Message message = req.body(Message.class);
        return status(CREATED)
            .body(tx.execute(s -> {
                    this.jdbcTemplate.update("INSERT INTO message(text) VALUES(?)", message.getText());
                    return message;
                }
            ));
    }

    ServerResponse init(ServerRequest req) {
        this.jdbcTemplate.execute("TRUNCATE TABLE message");
        return ok().body("");
    }
}
