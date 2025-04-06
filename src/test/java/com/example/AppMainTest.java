package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class AppMainTest {

    @Test
    public void testMain() {
        SpringApplication app = new SpringApplication(App.class);
        app.setWebApplicationType(WebApplicationType.SERVLET);

        Map<String, Object> props = new HashMap<>();
        props.put("server.port", 0); // Рандомный порт, чтобы не было конфликтов
        app.setDefaultProperties(props);

        try (ConfigurableApplicationContext ctx = app.run()) {
            // Если сервер стартует - тест успешный
        }
    }
}

