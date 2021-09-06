package top.zekozhang.demo.temperaturefetcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 温度获取服务入口
 *
 * @author Zhang Yun
 * @date 2021-09-04 14:14
 */
@SpringBootApplication
@EnableCaching
public class TemperatureFetcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemperatureFetcherApplication.class, args);
    }

}
