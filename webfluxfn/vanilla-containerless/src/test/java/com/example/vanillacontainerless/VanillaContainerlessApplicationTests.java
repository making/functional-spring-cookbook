package com.example.vanillacontainerless;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.SocketUtils;
import reactor.netty.DisposableServer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class VanillaContainerlessApplicationTests {

    int port;

    WebTestClient webTestClient;

    DisposableServer disposableServer;

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Before
    public void before() throws Exception {
        this.port = SocketUtils.findAvailableTcpPort();
        CountDownLatch latch = new CountDownLatch(1);
        this.executor.execute(() -> new VanillaContainerlessApplication()
            .startServer(port, disposableServer -> {
                this.disposableServer = disposableServer;
                latch.countDown();
            }));
        latch.await();
        this.webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + port)
            .build();
    }

    @After
    public void after() {
        this.disposableServer.dispose();
        this.executor.shutdown();
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
