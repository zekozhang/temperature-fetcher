package top.zekozhang.demo.temperaturefetcher.cache;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import top.zekozhang.demo.temperaturefetcher.constant.LocationConstant;
import top.zekozhang.demo.temperaturefetcher.util.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 中国省市区缓存服务
 * 缓存所有省市区Code信息
 * 考虑一次性获取所有省市区信息会同时发送大量HTTP请求，所以使用省市区分别缓存的方式
 *
 * @author Zhang Yun
 * @date 2021-09-04 22:09
 */
@Slf4j
@Component
public class LocationCache {
    /**
     * 返回值通用Key
     */
    private static final String COMMON_RETURN_KEY = "message";
    /**
     * 获取省份API
     */
    @Value("${weather.fetch.province.code.china}")
    private String getProvinceUrl;
    /**
     * 获取城市API
     */
    @Value("${weather.fetch.city.code.province}")
    private String getCityUrl;
    /**
     * 获取地区API
     */
    @Value("${weather.fetch.county.code.city}")
    private String getCountyUrl;

    /**
     * 获取所有的省份Code列表
     *
     * @return 省份Code列表
     */
    @Cacheable(value = LocationConstant.PROVINCE_CACHE_NAME, key = "'province_map'")
    public Map<String, String> getAllProvinces() {
        Map<String, String> codeMap = new HashMap<>();
        JSONObject resultJson;
        try {
            log.info("通过HTTP接口获取省份Code...");
            resultJson = HttpUtils.get(getProvinceUrl);

            if (!resultJson.isEmpty() && resultJson.getJSONObject(COMMON_RETURN_KEY) != null) {
                JSONObject codesJson = resultJson.getJSONObject(COMMON_RETURN_KEY);
                for (String code : codesJson.keySet()) {
                    codeMap.put(codesJson.getString(code), code);
                }
            }
        } catch (IOException e) {
            log.error("调用HTTP接口获取省份失败！", e);
        }

        return codeMap;
    }

    /**
     * 获取所有城市Code列表
     *
     * @param provinceName 省份名称
     * @return 所有城市列表
     */
    @Cacheable(value = LocationConstant.CITY_CACHE_NAME, key = "#provinceCode")
    public Map<String, String> getAllCityCodes(String provinceName, String provinceCode) {
        Map<String, String> codeMap = new HashMap<>();
        JSONObject resultJson;
        try {
            log.info("通过HTTP接口获取城市Code...");
            resultJson = HttpUtils.get(String.format(getCityUrl, provinceCode));

            if (!resultJson.isEmpty() && resultJson.getJSONObject(COMMON_RETURN_KEY) != null) {
                JSONObject codesJson = resultJson.getJSONObject(COMMON_RETURN_KEY);
                for (String code : codesJson.keySet()) {
                    codeMap.put(provinceName + "-" + codesJson.getString(code), provinceCode + code);
                }
            }
        } catch (IOException e) {
            log.error("调用HTTP接口获取城市失败！", e);
        }

        return codeMap;
    }

    /**
     * 获取所有区域Code列表
     *
     * @param cityName 城市名称
     * @param cityCode 城市Code
     * @return 所有区域Code列表
     */
    @Cacheable(value = LocationConstant.COUNTY_CACHE_NAME, key = "#cityCode")
    public Map<String, String> getAllCountyCodes(String cityName, String cityCode) {
        Map<String, String> codeMap = new HashMap<>();
        JSONObject resultJson;
        try {
            log.info("通过HTTP接口获取地区Code...");
            resultJson = HttpUtils.get(String.format(getCountyUrl, cityCode));

            if (!resultJson.isEmpty() && resultJson.getJSONObject(COMMON_RETURN_KEY) != null) {
                JSONObject codesJson = resultJson.getJSONObject(COMMON_RETURN_KEY);
                for (String code : codesJson.keySet()) {
                    codeMap.put(cityName + "-" + codesJson.getString(code), cityCode + code);
                }
            }
        } catch (IOException e) {
            log.error("调用HTTP接口获取地区失败！", e);
        }

        return codeMap;
    }
}
