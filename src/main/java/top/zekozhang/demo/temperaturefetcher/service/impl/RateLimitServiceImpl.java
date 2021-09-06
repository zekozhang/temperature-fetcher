package top.zekozhang.demo.temperaturefetcher.service.impl;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zekozhang.demo.temperaturefetcher.config.RateLimitConfig;
import top.zekozhang.demo.temperaturefetcher.service.IRateLimitService;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 限流服务实现
 *
 * @author Zhang Yun
 * @date 2021-09-04 14:18
 */
@Service
public class RateLimitServiceImpl implements IRateLimitService {

    private final RateLimitConfig config;

    private final RateLimiter rateLimiter;

    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    public RateLimitServiceImpl(RateLimitConfig config) {
        this.config = config;
        this.rateLimiter = RateLimiter.create(this.config.getPermitsPerSecond());
    }

    @Override
    public boolean tryAcquire() {

        return this.tryAcquire(1);
    }

    @Override
    public boolean tryAcquire(int permits) {
        synchronized (lock) {
            return rateLimiter.tryAcquire(permits);
        }
    }

    @Override
    public boolean acquire(long timeout) {
        return this.acquire(1, timeout);
    }

    @Override
    public boolean acquire(int permits, long timeout) {
        long start = System.currentTimeMillis();
        for (; ; ) {
            boolean tryAcquire = rateLimiter.tryAcquire(permits);
            if (tryAcquire) {
                return true;
            }
            long end = System.currentTimeMillis();
            if ((end - start) >= timeout) {
                return false;
            }
        }
    }

}
