package com.example.hellofunctionalbeanregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.ServerResponse.ok;

/**
 * https://docs.spring.io/spring-boot/docs/current/reference/html/howto-spring-boot-application.html#howto-customize-the-environment-or-application-context
 */
@SpringBootApplication
public class HelloFunctionalBeanRegistrationApplication implements ApplicationContextInitializer<GenericApplicationContext> {

    public static void main(String[] args) {
        SpringApplication.run(HelloFunctionalBeanRegistrationApplication.class, args);
    }

    RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
            .GET("/", req -> ok().body("Hello World!"))
            .POST("/echo", req -> ok().body(req.body(String.class)))
            .build();
    }

    @Override
    public void initialize(GenericApplicationContext context) {
        context.registerBean(RouterFunction.class, this::routes);
    }
}
