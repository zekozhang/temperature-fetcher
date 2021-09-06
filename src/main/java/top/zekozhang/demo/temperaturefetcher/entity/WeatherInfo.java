package top.zekozhang.demo.temperaturefetcher.entity;

import lombok.Data;

/**
 * 天气信息Bean
 *
 * @author Zhang Yun
 * @date 2021-09-05 13:28
 */
@Data
public class WeatherInfo {
    /**
     * 城市名称
     */
    private String city;

    /**
     * 城市Code
     */
    private String cityid;

    /**
     * 气温
     */
    private double temp;

    private String WD;

    private String WS;

    private String SD;

    private String AP;

    private String njd;

    private String WSE;

    private String time;

    private String sm;

    private String isRadar;

    private String Radar;
}
