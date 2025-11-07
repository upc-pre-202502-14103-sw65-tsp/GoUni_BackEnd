package com.studentconnect.gouni.platform.shared.infrastructure.redis.ratelimit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class RedisRateLimiter {

    private final StringRedisTemplate redisTemplate;
    // Lua script: increment key, set expire when first seen, return 1 if under limit else 0
    private static final String LUA_SCRIPT = ""
            + "local current = redis.call('incr', KEYS[1])\n"
            + "if tonumber(current) == 1 then\n"
            + "  redis.call('expire', KEYS[1], ARGV[2])\n"
            + "end\n"
            + "if tonumber(current) > tonumber(ARGV[1]) then\n"
            + "  return 0\n"
            + "end\n"
            + "return 1";

    public RedisRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Intenta consumir un "slot" del limitador.
     * @param key clave única (por usuario o IP)
     * @param limit cantidad máxima de requests permitidos en la ventana
     * @param windowSeconds tamaño de la ventana en segundos
     * @return true si permitido, false si excede
     */
    public boolean tryAcquire(String key, int limit, int windowSeconds) {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>(LUA_SCRIPT, Long.class);
        Long result = redisTemplate.execute(script, Collections.singletonList(key),
                String.valueOf(limit), String.valueOf(windowSeconds));
        return result != null && result == 1L;
    }
}
