package com.example.hellovalidationyavi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.servlet.function.RouterFunction;

@SpringBootApplication
public class HelloValidationYaviApplication implements ApplicationContextInitializer<GenericApplicationContext> {

    public static void main(String[] args) {
        SpringApplication.run(HelloValidationYaviApplication.class, args);
    }

    @Override
    public void initialize(GenericApplicationContext context) {
        context.registerBean(MessageHandler.class);
        context.registerBean(RouterFunction.class, () -> context.getBean(MessageHandler.class).routes());
    }
}
