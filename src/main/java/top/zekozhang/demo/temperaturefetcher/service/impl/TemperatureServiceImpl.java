package top.zekozhang.demo.temperaturefetcher.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.zekozhang.demo.temperaturefetcher.exception.ServiceException;
import top.zekozhang.demo.temperaturefetcher.service.LocationService;
import top.zekozhang.demo.temperaturefetcher.service.TemperatureService;
import top.zekozhang.demo.temperaturefetcher.service.WeatherService;

import java.util.Optional;

/**
 * 气温服务
 * 主要用于处理气温相关数据
 *
 * @author Zhang Yun
 * @date 2021-09-04 9:35
 */
@Service
@Slf4j
public class TemperatureServiceImpl implements TemperatureService {
    private final LocationService locationService;

    private final WeatherService weatherService;

    public TemperatureServiceImpl(LocationService locationService, WeatherService weatherService) {
        this.locationService = locationService;
        this.weatherService = weatherService;
    }

    /**
     * 获取指定省市区的气温
     *
     * @param province 省份
     * @param city     城市
     * @param county   地区
     * @return 区域气温
     * @throws ServiceException 获取气温异常
     */
    @Override
    public Optional<Integer> getTemperature(String province, String city, String county) {
        String code = locationService.getCode(province, city, county);
        return Optional.of((int) Math.round(weatherService.getCountyWeather(code).getTemp()));
    }

}
