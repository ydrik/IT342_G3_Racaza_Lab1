package com.annimemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application Class for AnniMemo
 * Pet Health Tracking System
 * FRS Reference: Spring Boot Framework for building REST API
 */
@SpringBootApplication
public class AnniMemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnniMemoApplication.class, args);
        System.out.println("===========================================");
        System.out.println("AnniMemo Backend Application Started!");
        System.out.println("Server running on: http://localhost:8080");
        System.out.println("===========================================");
    }
}
