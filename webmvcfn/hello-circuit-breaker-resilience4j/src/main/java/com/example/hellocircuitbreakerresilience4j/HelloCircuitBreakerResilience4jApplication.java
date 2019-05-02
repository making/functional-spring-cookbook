package com.example.hellocircuitbreakerresilience4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.ServerResponse.ok;

@SpringBootApplication
public class HelloCircuitBreakerResilience4jApplication implements ApplicationContextInitializer<GenericApplicationContext> {

    public static void main(String[] args) {
        SpringApplication.run(HelloCircuitBreakerResilience4jApplication.class, args);
    }

    RouterFunction<ServerResponse> routes(HelloService helloService) {
        return RouterFunctions.route()
            .GET("/cb", req -> ok().body(helloService.hello()))
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
