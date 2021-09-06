package top.zekozhang.demo.temperaturefetcher.service;

import top.zekozhang.demo.temperaturefetcher.exception.ServiceException;

import java.util.Optional;

/**
 * 气温服务
 * 主要用于处理气温相关数据
 *
 * @author Zhang Yun
 * @date 2021-09-04 9:35
 */
public interface TemperatureService {
    Optional<Integer> getTemperature(String province, String city, String county) throws ServiceException;
}
