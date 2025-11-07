package com.studentconnect.gouni.platform.shared.infrastructure.ratelimit;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

class RateLimiterTest {

    @Test
    void allowsUpToMaxRequestsWithinWindowAndBlocksAfter() {
        AtomicLong now = new AtomicLong(0);
        SimpleRateLimiter limiter = new SimpleRateLimiter(3, 1000, now::get);

        assertTrue(limiter.tryAcquire("user-1"));
        assertTrue(limiter.tryAcquire("user-1"));
        assertTrue(limiter.tryAcquire("user-1"));
        assertFalse(limiter.tryAcquire("user-1"));

        now.addAndGet(1000);
        assertTrue(limiter.tryAcquire("user-1"));
        assertTrue(limiter.tryAcquire("user-1"));
        assertTrue(limiter.tryAcquire("user-1"));
        assertFalse(limiter.tryAcquire("user-1"));
    }

    @Test
    void rateLimitingIsPerKey() {
        AtomicLong now = new AtomicLong(0);
        SimpleRateLimiter limiter = new SimpleRateLimiter(2, 1000, now::get);

        assertTrue(limiter.tryAcquire("A"));
        assertTrue(limiter.tryAcquire("A"));
        assertFalse(limiter.tryAcquire("A"));

        // Different key should have independent limits
        assertTrue(limiter.tryAcquire("B"));
        assertTrue(limiter.tryAcquire("B"));
        assertFalse(limiter.tryAcquire("B"));

        // Advance time resets windows
        now.addAndGet(1000);
        assertTrue(limiter.tryAcquire("A"));
        assertTrue(limiter.tryAcquire("B"));
    }
}