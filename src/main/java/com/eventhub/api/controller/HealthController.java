package com.eventhub.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @Value("${app.version}")
    private String version;

    @Value("${app.environment}")
    private String environment;

    @GetMapping("/health")
    public Map<String, String> healthCheck() {
        return Map.of(
                "status", "UP",
                "version", version,
                "environment", environment
        );
    }
}