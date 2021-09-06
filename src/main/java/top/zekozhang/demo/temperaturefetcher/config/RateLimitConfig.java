package top.zekozhang.demo.temperaturefetcher.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 限流配置
 *
 * @author Zhang Yun
 * @date 2021-09-04 14:26
 */
@Data
@Component
public class RateLimitConfig {

    @Value("${ratelimit.doRateLimit:false}")
    private boolean doRateLimit = false;
    @Value("${ratelimit.waitTimeout:0}")
    private long waitTimeout;
    @Value("${ratelimit.permitsPerSecond:100}")
    private long permitsPerSecond;

}
