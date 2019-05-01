package com.example.hellocachingcaffeine;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.cache.CacheMono;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.time.Duration;

public class HelloService {

    private final Logger log = LoggerFactory.getLogger(HelloService.class);

    private final WebClient webClient;

    private final Cache<Integer, ? super Signal<? extends JsonNode>> cache = Caffeine.newBuilder()
        .maximumSize(100)
        .expireAfterWrite(Duration.ofSeconds(10))
        .removalListener((key, value, cause) -> log.info("Remove cache({}) ... {}", key, cause))
        .build();

    public HelloService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public Mono<JsonNode> hello(int delay) {
        return CacheMono.lookup(this.cache.asMap(), delay)
            .onCacheMissResume(() -> this.webClient.get()
                .uri("https://httpbin.org/delay/{n}", delay)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .log("httpbin"))
            .log("cache");
    }
}
