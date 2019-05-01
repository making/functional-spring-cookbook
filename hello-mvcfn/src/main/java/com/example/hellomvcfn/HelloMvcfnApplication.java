package com.example.hellomvcfn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.ServerResponse.ok;

@SpringBootApplication
public class HelloMvcfnApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloMvcfnApplication.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
            .GET("/", req -> ok().body("Hello World!"))
            .POST("/echo", req -> ok().body(req.body(String.class)))
            .build();
    }

}
