package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    public void testMainMethod() throws IOException {
        System.out.println("Test application is running on port: " + port);
        // Запишем порт в файл, чтобы потом в Jenkins использовать
        Files.write(Paths.get("target/test-application-port.txt"), String.valueOf(port).getBytes());

        // Запускаем приложение
        App.main(new String[]{});
    }
}

