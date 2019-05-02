# functional-spring-cookbook
Functional Spring Cookbook

**Disclaimer**

Samples in this repo try to use functions as much as possible rather than annotations.
This is not always the best practice.

## Spring WebFlux.fn

### [hello-fluxfn](webfluxfn/hello-fluxfn)

This sample shows the simplest WebFlux.fn application.

### [hello-functional-bean-registration](webfluxfn/hello-functional-bean-registration)

This sample shows how to use Functional Bean Registration instead of `@Bean` annotation config.

### [hello-http-client](webfluxfn/hello-http-client)

This sample shows how to use a http client (`WebClient`).

### [hello-retry-simple](webfluxfn/hello-retry-simple)

This sample shows how to use a simple `retry` operator.

### [hello-retry-advanced](webfluxfn/hello-retry-advanced)

This sample shows how to use `reactor.retry.Retry` from [reactor-addons](https://github.com/reactor/reactor-addons) instead of `retry` operators.

### [hello-caching-chm](webfluxfn/hello-caching-chm)

This sample shows how to cache a result using `java.util.concurrent.ConcurrentHashMap` and `reactor.cache.CacheMono` from [reactor-addons](https://github.com/reactor/reactor-addons).

### [hello-caching-caffeine](webfluxfn/hello-caching-caffeine)

This sample shows how to cache a result using [Caffeine](https://github.com/ben-manes/caffeine) and `reactor.cache.CacheMono` from [reactor-addons](https://github.com/reactor/reactor-addons).

### [hello-validation-bv](webfluxfn/hello-validation-bv)

This sample shows how to validate an input using Bean Validation.

### [hello-validation-yavi](webfluxfn/hello-validation-yavi)

This sample shows how to validate an input using [YAVI](https://github.com/making/yavi).

### [hello-circuit-breaker-hystrix](webfluxfn/hello-circuit-breaker-hystrix)

This sample shows how to use circuit breaker using [Spring Cloud Circuit Breaker](https://github.com/spring-cloud-incubator/spring-cloud-circuitbreaker) and [Hystrix](https://github.com/Netflix/Hystrix)

### [hello-circuit-breaker-resilience4j](webfluxfn/hello-circuit-breaker-resilience4j)

This sample shows how to use circuit breaker using [Spring Cloud Circuit Breaker](https://github.com/spring-cloud-incubator/spring-cloud-circuitbreaker) and [Resilience4j](https://github.com/resilience4j)

### [hello-db-r2dbc-client-h2](webfluxfn/hello-db-r2dbc-client-h2)

This sample shows how to access H2 database using [R2DBC](https://r2dbc.io/) and [R2DBC Client](https://github.com/r2dbc/r2dbc-client)

### [hello-db-r2dbc-database-client-h2](webfluxfn/hello-db-r2dbc-database-client-h2)

This sample shows how to access H2 database using [R2DBC](https://r2dbc.io/) and `DatabaseClient` from [Spring Data R2DBC](https://github.com/spring-projects/spring-data-r2dbc)

## Spring WebMvc.fn

### [hello-mvcfn](webmvcfn/hello-mvcfn)

This sample shows the simplest WebMvc.fn application.

### [hello-functional-bean-registration](webmvcfn/hello-functional-bean-registration)

This sample shows how to use Functional Bean Registration instead of `@Bean` annotation config.

### [hello-validation-yavi](webmvcfn/hello-validation-yavi)

This sample shows how to validate an input using [YAVI](https://github.com/making/yavi).

### [hello-circuit-breaker-hystrix](webmvcfn/hello-circuit-breaker-hystrix)

This sample shows how to use circuit breaker using [Spring Cloud Circuit Breaker](https://github.com/spring-cloud-incubator/spring-cloud-circuitbreaker) and [Hystrix](https://github.com/Netflix/Hystrix)

### [hello-circuit-breaker-resilience4j](webmvcfn/hello-circuit-breaker-resilience4j)

This sample shows how to use circuit breaker using [Spring Cloud Circuit Breaker](https://github.com/spring-cloud-incubator/spring-cloud-circuitbreaker) and [Resilience4j](https://github.com/resilience4j)

