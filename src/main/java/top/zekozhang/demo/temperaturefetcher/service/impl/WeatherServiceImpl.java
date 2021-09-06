package top.zekozhang.demo.temperaturefetcher.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.zekozhang.demo.temperaturefetcher.cache.WeatherCache;
import top.zekozhang.demo.temperaturefetcher.entity.WeatherInfo;
import top.zekozhang.demo.temperaturefetcher.service.WeatherService;

/**
 * 气温服务
 * 主要用于处理气温相关数据
 *
 * @author Zhang Yun
 * @date 2021-09-04 9:35
 */
@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {
    /**
     * 气温缓存服务
     */
    private final WeatherCache weatherCache;

    public WeatherServiceImpl(WeatherCache weatherCache) {
        this.weatherCache = weatherCache;
    }

    /**
     * 获取指定区域温度
     *
     * @param code 区域代码
     * @return 区域温度
     */
    @Override
    public WeatherInfo getCountyWeather(String code) {
        return weatherCache.getCountyWeather(code);
    }
}
