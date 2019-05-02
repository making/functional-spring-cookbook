package com.example.vanillafluxfn;

import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

public class VanillaFluxfnApplication {

    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        int port = Optional.ofNullable(System.getenv("PORT"))
            .map(Integer::parseInt)
            .orElse(8080);
        GenericApplicationContext context = new VanillaFluxfnApplication().run();
        startServer(port, context, disposableServer -> {
            long elapsed = System.currentTimeMillis() - begin;
            LoggerFactory.getLogger(VanillaFluxfnApplication.class).info("Started in {} seconds",
                elapsed / 1000.0);
        });
    }

    RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
            .GET("/", req -> ok().syncBody("Hello World!"))
            .POST("/echo", req -> ok().body(req.bodyToMono(String.class), String.class))
            .build();
    }

    GenericApplicationContext run() {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("webHandler", WebHandler.class, () -> RouterFunctions.toWebHandler(this.routes()));
        context.refresh();
        context.registerShutdownHook();
        return context;
    }

    static void startServer(int port, GenericApplicationContext context, Consumer<DisposableServer> onStart) {
        HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(context).build();
        HttpServer httpServer = HttpServer.create().host("0.0.0.0").port(port).handle(new ReactorHttpHandlerAdapter(httpHandler));
        httpServer.bindUntilJavaShutdown(Duration.ofSeconds(3), onStart);
    }

}
