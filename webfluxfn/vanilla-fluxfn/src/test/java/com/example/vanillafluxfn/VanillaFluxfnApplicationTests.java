package com.example.vanillafluxfn;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.SocketUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class VanillaFluxfnApplicationTests {

    int port;

    GenericApplicationContext context;

    WebTestClient webTestClient;

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Before
    public void before() throws Exception {
        this.port = SocketUtils.findAvailableTcpPort();
        this.context = new VanillaFluxfnApplication().run();
        CountDownLatch latch = new CountDownLatch(1);
        this.executor.execute(() -> VanillaFluxfnApplication.startServer(port, context, s -> latch.countDown()));
        latch.await();
        this.webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + port)
            .build();
    }

    @After
    public void after() {
        this.executor.shutdown();
        this.context.close();
    }

    @Test
    public void hello() {
        webTestClient.get()
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .consumeWith(result -> {
                String body = result.getResponseBody();
                assertThat(body).isEqualTo("Hello World!");
            });
    }

    @Test
    public void echo() {
        webTestClient.post()
            .uri("/echo")
            .syncBody("Hello!")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .consumeWith(result -> {
                String body = result.getResponseBody();
                assertThat(body).isEqualTo("Hello!");
            });
    }
}
