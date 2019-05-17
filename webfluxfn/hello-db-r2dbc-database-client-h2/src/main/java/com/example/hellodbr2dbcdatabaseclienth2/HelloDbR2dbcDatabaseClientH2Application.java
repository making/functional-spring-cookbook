package com.example.hellodbr2dbcdatabaseclienth2;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.RouterFunction;

import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;
import static io.r2dbc.spi.ConnectionFactoryOptions.DRIVER;
import static io.r2dbc.spi.ConnectionFactoryOptions.PROTOCOL;

@SpringBootApplication
public class HelloDbR2dbcDatabaseClientH2Application implements ApplicationContextInitializer<GenericApplicationContext> {

    public static void main(String[] args) {
        SpringApplication.run(HelloDbR2dbcDatabaseClientH2Application.class, args);
    }

    @Override
    public void initialize(GenericApplicationContext context) {
        context.registerBean(ConnectionFactory.class, () -> ConnectionFactories.get(ConnectionFactoryOptions.builder()
            .option(DRIVER, "h2")
            .option(PROTOCOL, "file")
            .option(DATABASE, "./target/demo")
            .build()));
        context.registerBean(DatabaseClient.class, () -> DatabaseClient.create(context.getBean(ConnectionFactory.class)));
        context.registerBean(ReactiveTransactionManager.class, () -> new R2dbcTransactionManager(context.getBean(ConnectionFactory.class)));
        context.registerBean(TransactionalOperator.class, () -> TransactionalOperator.create(context.getBean(ReactiveTransactionManager.class)));
        context.registerBean(MessageHandler.class);
        context.registerBean(RouterFunction.class, () -> context.getBean(MessageHandler.class).routes());
        context.registerBean(InitializingBean.class, () -> () -> context.getBean(DatabaseClient.class)
            .execute()
            .sql("CREATE TABLE IF NOT EXISTS message (id INT PRIMARY KEY AUTO_INCREMENT, text VARCHAR(255) NOT NULL, created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)")
            .then()
            .block());
    }
}
