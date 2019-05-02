package com.example.hellocircuitbreakerresilience4j;

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
        return this.webClient.get()
            .retrieve()
            .bodyToFlux(String.class)
            .transform(x -> this.circuitBreakerFactory.create("hello")
                .run(x, t -> Flux.just("Fallback!")));
    }
}
