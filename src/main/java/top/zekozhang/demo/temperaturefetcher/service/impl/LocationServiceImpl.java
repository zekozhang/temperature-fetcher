package top.zekozhang.demo.temperaturefetcher.service.impl;

import org.springframework.stereotype.Service;
import top.zekozhang.demo.temperaturefetcher.cache.LocationCache;
import top.zekozhang.demo.temperaturefetcher.enums.ErrorCodeEnum;
import top.zekozhang.demo.temperaturefetcher.exception.ServiceException;
import top.zekozhang.demo.temperaturefetcher.service.LocationService;

import java.util.Map;

/**
 * 中国地区处理服务
 *
 * @author Zhang Yun
 * @date 2021-09-04 22:07
 */
@Service
public class LocationServiceImpl implements LocationService {
    private final LocationCache locationCache;

    public LocationServiceImpl(LocationCache locationCache) {
        this.locationCache = locationCache;
    }

    /**
     * 获取当前区域的Code
     *
     * @param province 省份名称
     * @param city     城市名称
     * @param county   地区名称
     * @return 区域Code
     * @throws ServiceException 获取省市区失败
     */
    @Override
    public String getCode(String province, String city, String county) throws ServiceException {
        // 获取所有省份
        Map<String, String> provinceMap = locationCache.getAllProvinces();
        if (provinceMap != null) {
            // 获取省份Code
            String provinceCode = provinceMap.get(province);
            if (provinceCode == null) {
                throw new ServiceException(ErrorCodeEnum.GET_PROVINCE_ERROR);
            } else {
                // 获取当前省份的所有城市
                Map<String, String> cityMap = locationCache.getAllCityCodes(province, provinceCode);
                if (cityMap != null) {
                    // 获取城市Code
                    String cityCode = cityMap.get(String.join("-", province, city));
                    if (cityCode == null) {
                        throw new ServiceException(ErrorCodeEnum.GET_CITY_ERROR);
                    } else {
                        // 获取当前城市的所有区域Code
                        Map<String, String> countyMap = locationCache.getAllCountyCodes(String.join("-", province, city), cityCode);
                        if (countyMap != null) {
                            // 获取区域Code
                            String countyCode = countyMap.get(String.join("-", province, city, county));
                            if (countyCode == null) {
                                throw new ServiceException(ErrorCodeEnum.GET_COUNTY_ERROR);
                            } else {
                                return countyCode;
                            }
                        } else {
                            throw new ServiceException(ErrorCodeEnum.GET_COUNTY_ERROR);
                        }
                    }
                } else {
                    throw new ServiceException(ErrorCodeEnum.GET_CITY_ERROR);
                }
            }
        } else {
            throw new ServiceException(ErrorCodeEnum.GET_PROVINCE_ERROR);
        }
    }
}
