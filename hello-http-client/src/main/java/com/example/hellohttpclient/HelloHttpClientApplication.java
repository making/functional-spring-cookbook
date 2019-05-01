package com.example.hellohttpclient;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class HelloHttpClientApplication implements ApplicationContextInitializer<GenericApplicationContext> {

    public static void main(String[] args) {
        SpringApplication.run(HelloHttpClientApplication.class, args);
    }

    RouterFunction<ServerResponse> routes(WebClient.Builder builder) {
        WebClient webClient = builder.build();
        return RouterFunctions.route()
            .GET("/get", req -> {
                Mono<JsonNode> body = webClient.get()
                    .uri("https://httpbin.org/get")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .log("httpbin");
                return ok().body(body, JsonNode.class);
            })
            .build();
    }

    @Override
    public void initialize(GenericApplicationContext context) {
        context.registerBean(RouterFunction.class, () -> {
            WebClient.Builder builder = context.getBean(WebClient.Builder.class);
            return this.routes(builder);
        });
    }
}
