package com.crise.demoj.aspect;

import com.crise.demoj.annotation.RateLimit;
import com.crise.demoj.dto.api.ResultCode;
import com.crise.demoj.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class RateLimitAspect {
    private final Map<String, SlidingWindow> limiters = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint pjp, RateLimit rateLimit) throws Throwable {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs != null ? attrs.getRequest() : null;
        String ip = request != null ? request.getRemoteAddr() : "unknown";
        String method = pjp.getSignature().toShortString();
        String key = ip + ":" + method;

        SlidingWindow window = limiters.computeIfAbsent(key,
                k -> new SlidingWindow(rateLimit.count(), rateLimit.timeWindow(), rateLimit.timeUnit()));

        if (!window.tryAcquire()) {
            log.warn("Rate limit exceeded: key={}, count={}, window={}{}", key,
                    rateLimit.count(), rateLimit.timeWindow(), rateLimit.timeUnit());
            throw new UserException(ResultCode.FAILED, "请求过于频繁，请稍后再试");
        }

        return pjp.proceed();
    }

    private static class SlidingWindow {
        private final int maxCount;
        private final long windowNanos;
        private final long[] slots;
        private int index;

/**
 * Constructor for the SlidingWindow class that initializes a sliding window with specified parameters.
 *
 * @param maxCount The maximum number of slots in the sliding window
 * @param timeWindow The duration of the time window
 * @param timeUnit The time unit for the time window (e.g., seconds, milliseconds, nanoseconds)
 */
        SlidingWindow(int maxCount, long timeWindow, TimeUnit timeUnit) {
    // Initialize the maximum count of slots in the window
            this.maxCount = maxCount;
    // Convert the time window duration to nanoseconds for consistent time handling
            this.windowNanos = timeUnit.toNanos(timeWindow);
            this.slots = new long[maxCount];
            this.index = 0;
        }

        synchronized boolean tryAcquire() {
            long now = System.nanoTime();
            long boundary = now - windowNanos;
            int validCount = 0;

            for (int i = 0; i < maxCount; i++) {
                if (slots[i] > boundary) {
                    validCount++;
                }
            }

            if (validCount < maxCount) {
                slots[index] = now;
                index = (index + 1) % maxCount;
                return true;
            }
            return false;
        }
    }
}
