package com.example.hellodbtxtemplateh2;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.function.RouterFunction;

@SpringBootApplication
public class HelloDbTxTemplateH2Application implements ApplicationContextInitializer<GenericApplicationContext> {

    public static void main(String[] args) {
        SpringApplication.run(HelloDbTxTemplateH2Application.class, args);
    }

    @Override
    public void initialize(GenericApplicationContext context) {
        context.registerBean(MessageHandler.class);
        context.registerBean(RouterFunction.class, () -> context.getBean(MessageHandler.class).routes());
        context.registerBean(InitializingBean.class, () -> () -> {
            final JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS message (id INT PRIMARY KEY AUTO_INCREMENT, text VARCHAR(255) NOT NULL, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
        });
    }
}
