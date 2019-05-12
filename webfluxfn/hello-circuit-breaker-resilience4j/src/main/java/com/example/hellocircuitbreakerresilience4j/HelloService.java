package com.example.hellocircuitbreakerresilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.core.EventProcessor;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.circuitbreaker.commons.ReactiveCircuitBreakerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class HelloService {

    private final WebClient webClient;

    private final CircuitBreaker circuitBreaker;

    private final Logger log = LoggerFactory.getLogger(HelloService.class);

    public HelloService(WebClient.Builder builder, FragileApi fragileApi, ReactiveCircuitBreakerFactory circuitBreakerFactory) {
        this.webClient = builder
            .baseUrl("http://localhost:" + fragileApi.getPort())
            .build();
        this.circuitBreaker = CircuitBreaker.of("hello", CircuitBreakerConfig
            .custom()
            .failureRateThreshold(50)
            .ringBufferSizeInClosedState(30)
            .ringBufferSizeInHalfOpenState(20)
            .waitDurationInOpenState(Duration.ofSeconds(3))
            .build());
        CircuitBreaker.EventPublisher eventPublisher = this.circuitBreaker.getEventPublisher();
        if (!((EventProcessor) eventPublisher).hasConsumers()) {
            eventPublisher.onStateTransition(event -> log.info("{}: {}", event.getCircuitBreakerName(), event.getStateTransition()));
        }
    }

    public Mono<String> hello() {
        return this.webClient.get()
            .retrieve()
            .bodyToMono(String.class)
            .transform(CircuitBreakerOperator.of(this.circuitBreaker))
            .onErrorResume(e -> Mono.just("Fallback!"));
    }
}
