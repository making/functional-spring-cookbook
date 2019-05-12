package com.example.hellohttpclient;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.ServerResponse.ok;


@SpringBootApplication
public class HelloHttpClientApplication implements ApplicationContextInitializer<GenericApplicationContext> {

    public static void main(String[] args) {
        SpringApplication.run(HelloHttpClientApplication.class, args);
    }

    RouterFunction<ServerResponse> routes(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        return RouterFunctions.route()
            .GET("/get", req -> {
                final JsonNode body = restTemplate.getForObject("https://httpbin.org/get", JsonNode.class);
                return ok().body(body);
            })
            .build();
    }

    @Override
    public void initialize(GenericApplicationContext context) {
        context.registerBean(RouterFunction.class, () -> {
            final RestTemplateBuilder builder = context.getBean(RestTemplateBuilder.class);
            return this.routes(builder);
        });
    }
}