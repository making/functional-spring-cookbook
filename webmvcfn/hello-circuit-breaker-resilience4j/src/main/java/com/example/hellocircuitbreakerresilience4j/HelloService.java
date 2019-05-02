package com.example.hellocircuitbreakerresilience4j;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.circuitbreaker.commons.CircuitBreakerFactory;
import org.springframework.web.client.RestTemplate;

public class HelloService {

    private final RestTemplate restTemplate;

    private final CircuitBreakerFactory circuitBreakerFactory;

    public HelloService(RestTemplateBuilder builder, FragileApi fragileApi, CircuitBreakerFactory circuitBreakerFactory) {
        this.restTemplate = builder
            .rootUri("http://localhost:" + fragileApi.getPort())
            .build();
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public String hello() {
        return this.circuitBreakerFactory.create("hello")
            .run(() -> this.restTemplate.getForObject("/", String.class), t -> "Fallback!");
    }
}
