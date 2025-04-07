package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    public void testAppRunningOnRandomPort() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("http://localhost:" + port + "/", String.class);
        assertThat(response).contains("App running in Kubernetes via Spring Boot!");
    }
}

