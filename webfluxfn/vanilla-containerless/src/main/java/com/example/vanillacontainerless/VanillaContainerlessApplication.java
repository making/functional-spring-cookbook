package com.example.vanillacontainerless;


import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

public class VanillaContainerlessApplication {

    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        int port = Optional.ofNullable(System.getenv("PORT"))
            .map(Integer::parseInt)
            .orElse(8080);

        new VanillaContainerlessApplication()
            .startServer(port, disposableServer -> {
                long elapsed = System.currentTimeMillis() - begin;
                LoggerFactory.getLogger(VanillaContainerlessApplication.class).info("Started in {} seconds",
                    elapsed / 1000.0);
            });
    }

    RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
            .GET("/", req -> ok().syncBody("Hello World!"))
            .POST("/echo", req -> ok().body(req.bodyToMono(String.class), String.class))
            .build();
    }

    void startServer(int port, Consumer<DisposableServer> onStart) {
        HttpHandler httpHandler = RouterFunctions.toHttpHandler(this.routes(), HandlerStrategies.builder().build());
        HttpServer httpServer = HttpServer.create().host("0.0.0.0").port(port).handle(new ReactorHttpHandlerAdapter(httpHandler));
        httpServer.bindUntilJavaShutdown(Duration.ofSeconds(3), onStart);
    }
}
