package com.chennian.storytelling.admin.controller.conference;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 会议系统健康检查组件
 */
@Component
class ConferenceHealthIndicator implements HealthIndicator {

    private final ConferenceController conferenceController;

    public ConferenceHealthIndicator(ConferenceController conferenceController) {
        this.conferenceController = conferenceController;
    }

    @Override
    public Health health() {
        try {
            // 检查系统健康状态
            if (conferenceController.systemHealthy) {
                return Health.up()
                        .withDetail("status", "Conference system is healthy")
                        .withDetail("timestamp", LocalDateTime.now())
                        .build();
            } else {
                return Health.down()
                        .withDetail("status", "Conference system is unhealthy")
                        .withDetail("timestamp", LocalDateTime.now())
                        .withDetails(conferenceController.healthMetrics)
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .withDetail("timestamp", LocalDateTime.now())
                    .build();
        }
    }
}
