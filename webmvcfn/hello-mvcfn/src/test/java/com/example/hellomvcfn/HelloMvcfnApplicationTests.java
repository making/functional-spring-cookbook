package com.example.hellomvcfn;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloMvcfnApplicationTests {

    @LocalServerPort
    int port;

    @Autowired
    WebTestClient webTestClient;

    @Before
    public void before() {
        webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + port)
            .build();
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
