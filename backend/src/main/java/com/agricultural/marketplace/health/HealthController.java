package com.agricultural.marketplace.health;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lightweight health endpoint for load balancers and uptime checks.
 */
@RestController
public class HealthController {

    private final String serviceName;

    public HealthController(@Value("${spring.application.name:agricultural-marketplace-api}") String serviceName) {
        this.serviceName = serviceName;
    }

    @GetMapping("/api/v1/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", serviceName,
                "timestamp", LocalDateTime.now()));
    }
}
