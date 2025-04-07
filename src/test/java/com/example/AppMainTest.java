package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class AppMainTest {

    @Test
    public void testMainMethod() {
        new SpringApplicationBuilder(App.class)
            .properties("server.port=0")
            .run();
    }
}

