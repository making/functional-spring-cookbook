package com.example.hellocircuitbreakerhystrix;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class HelloCircuitBreakerHystrixApplication implements ApplicationContextInitializer<GenericApplicationContext> {

    public static void main(String[] args) {
        SpringApplication.run(HelloCircuitBreakerHystrixApplication.class, args);
    }

    RouterFunction<ServerResponse> routes(HelloService helloService) {
        return RouterFunctions.route()
            .GET("/cb", req -> ok().body(helloService.hello(), String.class))
            .build();
    }

    @Override
    public void initialize(GenericApplicationContext context) {
        context.registerBean(FragileApi.class);
        context.registerBean(HelloService.class);
        context.registerBean(RouterFunction.class, () -> this.routes(context.getBean(HelloService.class)));
        context.registerBean(CircuitBreakerFactoryCustomizer.class);
    }
}
