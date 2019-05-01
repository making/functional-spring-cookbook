package com.example.hellocachingchm;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.cache.CacheMono;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HelloService {

    private final WebClient webClient;

    private final ConcurrentMap<Integer, ? super Signal<? extends JsonNode>> cache = new ConcurrentHashMap<>();

    public HelloService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public Mono<JsonNode> hello(int delay) {
        return CacheMono.lookup(this.cache, delay)
            .onCacheMissResume(() -> this.webClient.get()
                .uri("https://httpbin.org/delay/{n}", delay)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .log("httpbin"))
            .log("cache");
    }
}
