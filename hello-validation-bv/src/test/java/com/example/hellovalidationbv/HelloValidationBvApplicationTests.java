package com.example.hellovalidationbv;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloValidationBvApplicationTests {

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
    public void testMessage() throws Exception {
        this.webTestClient.post()
            .uri("/messages") //
            .syncBody(new Message("Hello"))
            .exchange() //
            .expectStatus().isOk() //
            .expectBody(String.class).isEqualTo("{\"text\":\"Hello\"}");

        this.webTestClient.get()
            .uri("/messages") //
            .exchange() //
            .expectStatus().isOk() //
            .expectBody(String.class).isEqualTo("[{\"text\":\"Hello\"}]");
    }


    @Test
    public void testInvalidMessage() throws Exception {
        this.webTestClient.post()
            .uri("/messages") //
            .syncBody(new Message("HelloHello"))
            .exchange() //
            .expectStatus().isBadRequest();
    }
}

