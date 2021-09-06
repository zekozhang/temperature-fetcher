package top.zekozhang.demo.temperaturefetcher.enums;

/**
 * 错误码枚举
 *
 * @author Zhang Yun
 * @date 2021-09-04 15:11
 */
public enum ErrorCodeEnum {
    /**
     * 省份获取异常
     */
    GET_PROVINCE_ERROR(4001, "省份获取异常"),
    /**
     * 城市获取异常
     */
    GET_CITY_ERROR(4002, "城市获取异常"),
    /**
     * 地区获取异常
     */
    GET_COUNTY_ERROR(4003, "地区获取异常"),
    /**
     * 地区获取异常
     */
    GET_TEMPERATURE_ERROR(4004, "地区气温获取异常"),
    /**
     * 流量超限
     */
    OVER_RATE_LIMIT(4050, "流量超限"),

    /**
     * 系统异常
     */
    SYSTEM_EXCEPTION(9000, "系统异常");

    /**
     * key
     */
    private final int key;
    /**
     * 描述
     */
    private final String desc;

    ErrorCodeEnum(final int key, final String desc) {
        this.key = key;
        this.desc = desc;
    }

    /**
     * 键
     *
     * @return 键
     */
    public int key() {
        return key;
    }

    /**
     * 描述
     *
     * @return 描述
     */
    public String desc() {
        return desc;
    }
}
