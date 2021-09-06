package top.zekozhang.demo.temperaturefetcher.service;

import top.zekozhang.demo.temperaturefetcher.exception.ServiceException;

/**
 * 中国地区处理服务接口
 *
 * @author Zhang Yun
 * @date 2021-09-05 0:02
 */
public interface LocationService {
    String getCode(String province, String city, String county) throws ServiceException;
}
