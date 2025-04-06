package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class AppMainTest {

    @Test
    public void testMainMethod() {
        // Настраиваем приложение на запуск с рандомным портом
        SpringApplication app = new SpringApplication(App.class);
        app.setWebApplicationType(WebApplicationType.SERVLET);

        Map<String, Object> props = new HashMap<>();
        props.put("server.port", 0); // RANDOM_PORT чтобы избежать конфликта
        app.setDefaultProperties(props);

        try (ConfigurableApplicationContext context = app.run()) {
            // Проверяем что контекст успешно поднялся
            assert context.isRunning();
        }
    }
}

