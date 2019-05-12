package com.example.hellodbtxtemplateh2;

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
public class HelloDbTxTemplateH2ApplicationTests {

    @LocalServerPort
    int port;

    @Autowired
    WebTestClient webTestClient;

    @Before
    public void before() {
        this.webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + port)
            .build();
        this.webTestClient.post()
            .uri("/init")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    public void testMessage() throws Exception {
        this.webTestClient.post()
            .uri("/messages") //
            .syncBody(new Message("Hello"))
            .exchange() //
            .expectStatus().isCreated() //
            .expectBody(String.class).isEqualTo("{\"text\":\"Hello\"}");

        this.webTestClient.get()
            .uri("/messages") //
            .exchange() //
            .expectStatus().isOk() //
            .expectBody(String.class).isEqualTo("[{\"text\":\"Hello\"}]");
    }

}
