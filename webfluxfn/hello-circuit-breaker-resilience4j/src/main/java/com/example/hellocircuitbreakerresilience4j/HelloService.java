package com.example.hellocircuitbreakerresilience4j;

import org.springframework.cloud.circuitbreaker.commons.ReactiveCircuitBreaker;
import org.springframework.cloud.circuitbreaker.commons.ReactiveCircuitBreakerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class HelloService {

    private final WebClient webClient;

    private final ReactiveCircuitBreakerFactory circuitBreakerFactory;

    public HelloService(WebClient.Builder builder, FragileApi fragileApi, ReactiveCircuitBreakerFactory circuitBreakerFactory) {
        this.webClient = builder
            .baseUrl("http://localhost:" + fragileApi.getPort())
            .build();
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public Flux<String> hello() {
        final ReactiveCircuitBreaker circuitBreaker = this.circuitBreakerFactory.create("hello");
        return circuitBreaker.run(this.webClient.get()
            .retrieve()
            .bodyToFlux(String.class), t -> Flux.just("Fallback!"));
    }
}
