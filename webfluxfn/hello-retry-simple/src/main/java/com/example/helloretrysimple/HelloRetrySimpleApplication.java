package com.example.helloretrysimple;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class HelloRetrySimpleApplication implements ApplicationContextInitializer<GenericApplicationContext> {

    public static void main(String[] args) {
        SpringApplication.run(HelloRetrySimpleApplication.class, args);
    }

    RouterFunction<ServerResponse> routes(WebClient.Builder builder) {
        final WebClient webClient = builder.build();
        return RouterFunctions.route()
            .GET("/delay", req -> {
                final Mono<JsonNode> body = webClient.get()
                    .uri("https://httpbin.org/delay/1")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .log("httpbin");
                return ok().body(body.retry(10, e -> e instanceof ReadTimeoutException), JsonNode.class);
            })
            .build();
    }

    @Override
    public void initialize(GenericApplicationContext context) {
        context.registerBean(ReactorClientHttpConnector.class,
            () -> new ReactorClientHttpConnector(HttpClient.create()
                .tcpConfiguration(tcpClient -> tcpClient
                    .doOnConnected(connection -> connection
                        .addHandler(new ReadTimeoutHandler(1200, TimeUnit.MILLISECONDS))))));
        context.registerBean(RouterFunction.class, () -> {
            final WebClient.Builder builder = context.getBean(WebClient.Builder.class);
            return this.routes(builder);
        });
    }
}
