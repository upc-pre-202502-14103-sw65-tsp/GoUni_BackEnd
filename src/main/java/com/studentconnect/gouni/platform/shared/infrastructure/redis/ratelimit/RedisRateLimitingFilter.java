package com.studentconnect.gouni.platform.shared.infrastructure.redis.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class RedisRateLimitingFilter extends OncePerRequestFilter {

    private final RedisRateLimiter rateLimiter;

    private final int limit;
    private final int windowSeconds;

    public RedisRateLimitingFilter(RedisRateLimiter rateLimiter, int limit, int windowSeconds) {
        this.rateLimiter = rateLimiter;
        this.limit = limit;
        this.windowSeconds = windowSeconds;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String key = buildKey(request);
        boolean allowed = rateLimiter.tryAcquire(key, limit, windowSeconds);

        if (!allowed) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // 429
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Too many requests\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String buildKey(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getName() != null) {
            // limitar por usuario autenticado (username/email)
            return "rate:user:" + sanitize(auth.getName());
        }
        // caer en IP client
        String ip = extractClientIp(request);
        return "rate:ip:" + sanitize(ip);
    }

    private String extractClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isBlank()) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String sanitize(String s) {
        if (s == null) return "anonymous";
        return s.replaceAll("[^a-zA-Z0-9@._:-]", "_");
    }
}
