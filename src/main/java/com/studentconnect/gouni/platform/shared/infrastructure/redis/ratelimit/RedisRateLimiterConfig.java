package com.studentconnect.gouni.platform.shared.infrastructure.redis.ratelimit;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisRateLimiterConfig {
    private static final int DEFAULT_LIMIT = 5;       // requests
    private static final int DEFAULT_WINDOW_SECONDS = 60; // segundos

    @Bean
    public FilterRegistrationBean<RedisRateLimitingFilter> rateLimitingFilterRegistration(RedisRateLimiter redisRateLimiter) {
        RedisRateLimitingFilter filter = new RedisRateLimitingFilter(redisRateLimiter, DEFAULT_LIMIT, DEFAULT_WINDOW_SECONDS);
        FilterRegistrationBean<RedisRateLimitingFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setName("redisRateLimitingFilter");
        registration.addUrlPatterns("/api/*");
        registration.setOrder(10);
        return registration;
    }
}
