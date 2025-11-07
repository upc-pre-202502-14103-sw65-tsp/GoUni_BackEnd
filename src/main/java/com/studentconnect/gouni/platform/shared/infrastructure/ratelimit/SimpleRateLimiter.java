package com.studentconnect.gouni.platform.shared.infrastructure.ratelimit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

/**
 * Simple fixed-window rate limiter per key.
 * Allows up to maxRequests within windowMillis for each key.
 */
public class SimpleRateLimiter {
    private final int maxRequests;
    private final long windowMillis;
    private final ConcurrentHashMap<String, Counter> counters = new ConcurrentHashMap<>();
    private final LongSupplier clockMillis;

    private static class Counter {
        long windowStart;
        int count;
        Counter(long windowStart, int count) {
            this.windowStart = windowStart;
            this.count = count;
        }
    }

    public SimpleRateLimiter(int maxRequests, long windowMillis) {
        this(maxRequests, windowMillis, System::currentTimeMillis);
    }

    public SimpleRateLimiter(int maxRequests, long windowMillis, LongSupplier clockMillis) {
        if (maxRequests <= 0) throw new IllegalArgumentException("maxRequests must be > 0");
        if (windowMillis <= 0) throw new IllegalArgumentException("windowMillis must be > 0");
        this.maxRequests = maxRequests;
        this.windowMillis = windowMillis;
        this.clockMillis = clockMillis;
    }

    /**
     * Try to acquire permission for the given key.
     * @return true if allowed, false if rate-limited
     */
    public boolean tryAcquire(String key) {
        final long now = clockMillis.getAsLong();
        final boolean[] allowed = new boolean[1];
        counters.compute(key, (k, ctr) -> {
            if (ctr == null) {
                allowed[0] = true;
                return new Counter(now, 1);
            }
            // Reset window if expired
            if (now - ctr.windowStart >= windowMillis) {
                ctr.windowStart = now;
                ctr.count = 1;
                allowed[0] = true;
                return ctr;
            }
            if (ctr.count < maxRequests) {
                ctr.count++;
                allowed[0] = true;
            } else {
                allowed[0] = false;
            }
            return ctr;
        });
        return allowed[0];
    }
}