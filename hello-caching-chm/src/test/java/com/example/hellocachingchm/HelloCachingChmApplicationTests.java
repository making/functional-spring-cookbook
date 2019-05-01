package com.example.hellocachingchm;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloCachingChmApplicationTests {

    @LocalServerPort
    int port;

    @Autowired
    WebTestClient webTestClient;

    @Before
    public void before() {
        webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + port)
            .responseTimeout(Duration.ofSeconds(10))
            .build();
    }

    @Test
    public void cache() {
        webTestClient.get()
            .uri("/cache")
            .exchange()
            .expectStatus().isOk()
            .expectBody(JsonNode.class)
            .consumeWith(result -> {
                JsonNode body = result.getResponseBody();
                assertThat(body.has("url")).isTrue();
                assertThat(body.get("url").asText()).isEqualTo("https://httpbin.org/delay/3");
            });
        webTestClient.get()
            .uri("/cache")
            .exchange()
            .expectStatus().isOk()
            .expectBody(JsonNode.class)
            .consumeWith(result -> {
                JsonNode body = result.getResponseBody();
                assertThat(body.has("url")).isTrue();
                assertThat(body.get("url").asText()).isEqualTo("https://httpbin.org/delay/3");
            });
    }
}

