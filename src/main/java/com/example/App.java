package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @GetMapping("/")
    public String hello() {
        return " App running in Kubernetes via Spring Boot!";
    }

    // 👇 Можно оставить метод add для тестов
    public static int add(int a, int b) {
        return a + b;
    }
}
