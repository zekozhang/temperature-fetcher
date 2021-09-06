package top.zekozhang.demo.temperaturefetcher.service;

import top.zekozhang.demo.temperaturefetcher.entity.WeatherInfo;

/**
 * 天气服务
 * 通过天气查询API获取天气信息并缓存天气信息
 *
 * @author Zhang Yun
 * @date 2021-09-04 9:35
 */
public interface WeatherService {
    WeatherInfo getCountyWeather(String code);
}
