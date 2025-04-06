package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    public void testAppRunningOnRandomPort() throws IOException {
        System.out.println("🌐 Test application is running on port: " + port);
        Files.write(Paths.get("target/test-application-port.txt"), String.valueOf(port).getBytes());

        // 🔥 Дополнительно: проверим доступность эндпоинта
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("http://localhost:" + port + "/", String.class);
        assertThat(response).contains("App running in Kubernetes via Spring Boot!");
    }
}
