package top.zekozhang.demo.temperaturefetcher.cache;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.zekozhang.demo.temperaturefetcher.constant.WeatherConstant;
import top.zekozhang.demo.temperaturefetcher.entity.WeatherInfo;
import top.zekozhang.demo.temperaturefetcher.enums.ErrorCodeEnum;
import top.zekozhang.demo.temperaturefetcher.exception.ServiceException;
import top.zekozhang.demo.temperaturefetcher.util.HttpUtils;

import java.io.IOException;

/**
 * 气温缓存服务
 * 获取和缓存气温信息
 *
 * @author Zhang Yun
 * @date 2021-09-04 9:35
 */
@Service
@Slf4j
public class WeatherCache {
    /**
     * 返回值通用Key
     */
    private static final String COMMON_RETURN_KEY = "message";
    /**
     * 获取气温API
     */
    @Value("${weather.fetch.county}")
    private String getWeatherUrl;

    /**
     * 获取指定区域温度
     *
     * @param code 区域代码
     * @return 区域温度
     */
    @Cacheable(value = WeatherConstant.WEATHER_CACHE_NAME, key = "#code")
    public WeatherInfo getCountyWeather(String code) {
        JSONObject weatherJson = null;
        try {
            log.info("通过HTTP接口获取天气信息...");
            weatherJson = HttpUtils.get(String.format(getWeatherUrl, code));

            if (!weatherJson.isEmpty() && weatherJson.getJSONObject(COMMON_RETURN_KEY) != null) {
                return weatherJson.getJSONObject(COMMON_RETURN_KEY).getJSONObject("weatherinfo").toJavaObject(WeatherInfo.class);
            }
        } catch (IOException e) {
            log.error("调用HTTP接口获取气温失败！", e);
        }
        throw new ServiceException(ErrorCodeEnum.GET_TEMPERATURE_ERROR, "获取气温异常");
    }
}
