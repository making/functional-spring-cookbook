package com.example.hellofluxfn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class HelloFluxfnApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloFluxfnApplication.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
            .GET("/", req -> ok().syncBody("Hello World!"))
            .POST("/echo", req -> ok().body(req.bodyToMono(String.class), String.class))
            .build();
    }

}
