# functional-spring-cookbook
Functional Spring Cookbook

**Disclaimer**

Samples in this repo try to use functions rather than annotations as much as possible.
This is not always the best practice.

## Spring WebFlux.fn

### [hello-fluxfn](hello-fluxfn)

This sample shows the simplest WebFlux.fn application.

### [hello-functional-bean-registration](hello-functional-bean-registration)

This sample shows how to use Functional Bean Registration instead of `@Bean` annotation config.

### [hello-http-client](hello-http-client)

This sample shows how to use a http client (`WebClient`).

### [hello-retry-simple](hello-retry-simple)

This sample shows how to use a simple `retry` operator.

### [hello-retry-advanced](hello-retry-advanced)

This sample shows how to use `reactor.retry.Retry` from [reactor-addons](https://github.com/reactor/reactor-addons) instead of `retry` operators.

### [hello-caching-chm](hello-caching-chm)

This sample shows how to cache a result using `java.util.concurrent.ConcurrentHashMap` and `reactor.cache.CacheMono` from [reactor-addons](https://github.com/reactor/reactor-addons).

### [hello-caching-caffeine](hello-caching-caffeine)

This sample shows how to cache a result using [Caffeine](https://github.com/ben-manes/caffeine) and `reactor.cache.CacheMono` from [reactor-addons](https://github.com/reactor/reactor-addons).

### [hello-validation-bv](hello-validation-bv)

This sample shows how to validate an input using Bean Validation.

### [hello-validation-yavi](hello-validation-yavi)

This sample shows how to validate an input using [YAVI](https://github.com/making/yavi).

## Spring WebMvc.fn

### [hello-mvcfn](hello-mvcfn)

This sample shows the simplest WebMvc.fn application.
