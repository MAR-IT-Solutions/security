package com.mar.solutions.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class HealthCheck {
    @Scheduled(fixedDelay = 300000)
    public void checkHealth() {
        log.info("Health check at " + System.currentTimeMillis());
    }
}
