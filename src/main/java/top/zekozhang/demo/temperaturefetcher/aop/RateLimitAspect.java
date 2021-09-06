package top.zekozhang.demo.temperaturefetcher.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.zekozhang.demo.temperaturefetcher.config.RateLimitConfig;
import top.zekozhang.demo.temperaturefetcher.enums.ErrorCodeEnum;
import top.zekozhang.demo.temperaturefetcher.exception.ServiceLimitException;
import top.zekozhang.demo.temperaturefetcher.service.IRateLimitService;

/**
 * 限流切面
 *
 * @author Zhang Yun
 * @date 2021-09-04 14:25
 */
@Slf4j
@Component
@Aspect
public class RateLimitAspect {

    private final RateLimitConfig config;

    private final IRateLimitService rateLimitService;

    @Autowired
    public RateLimitAspect(RateLimitConfig config, IRateLimitService rateLimitService) {
        this.config = config;
        this.rateLimitService = rateLimitService;
    }

    @Pointcut("execution(public * top.zekozhang.demo.temperaturefetcher.service.impl.TemperatureServiceImpl.get*(..))")
    public void executionMethod() {
    }

    @Around(value = "executionMethod()")
    public Object doRateLimit(ProceedingJoinPoint pjp) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("开始限流处理...");
        }
        Object result;
        // 判断是否限流
        try {
            if (config.isDoRateLimit()) {
                // 开启限流
                boolean acquireResult = false;
                // 1.查看是否配置超时时间
                if (config.getWaitTimeout() == 0L) {
                    // 2.获取令牌
                    acquireResult = rateLimitService.tryAcquire();
                } else {
                    // 2.获取令牌，超时时间内获取令牌
                    acquireResult = rateLimitService.acquire(config.getWaitTimeout());
                }
                if (acquireResult) {
                    log.info("成功获取令牌，可以执行方法{}", pjp.getSignature());
                    // 3.成功获取令牌，放行
                    result = pjp.proceed();
                } else {
                    // 3.失败获取令牌，抛出异常通知上层流量超限
                    // 超限抛出异常，按照Code Desc返回，如果是Web项目可以统一处理后返回前端
                    log.error("获取令牌失败，限制最大TPS为{}， 不允许执行方法{}", config.getPermitsPerSecond(), pjp.getSignature());
                    throw new ServiceLimitException(ErrorCodeEnum.OVER_RATE_LIMIT, "请求超限，请稍后重试！");
                }
            } else {
                log.info("未开启限流...");
                // 无开启限流，直接放行
                result = pjp.proceed();
            }
        } catch (Throwable e) {
            throw e;
        }

        if (log.isDebugEnabled()) {
            log.debug("限流处理切面结束!");
        }
        return result;
    }
}
