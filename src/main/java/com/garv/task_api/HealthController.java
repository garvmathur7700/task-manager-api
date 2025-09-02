package com.garv.task_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/actuator")
class HealthController {
    @GetMapping("/health")
    public Map<String, String> health() { return Map.of("status", "OK"); }
}