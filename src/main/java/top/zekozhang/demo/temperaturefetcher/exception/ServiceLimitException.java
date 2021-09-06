package top.zekozhang.demo.temperaturefetcher.exception;


import lombok.Getter;
import top.zekozhang.demo.temperaturefetcher.enums.ErrorCodeEnum;

/**
 * 服务限制类异常
 *
 * @author Zhang Yun
 * @date 2021-09-04 15:20
 */
@Getter
public class ServiceLimitException extends Throwable {

    /**
     * 异常代码
     */
    private final ErrorCodeEnum errorCodeEnum;

    /**
     * 异常说明
     */
    private final String desc;

    public ServiceLimitException() {
        this.errorCodeEnum = ErrorCodeEnum.SYSTEM_EXCEPTION;
        this.desc = ErrorCodeEnum.SYSTEM_EXCEPTION.desc();
    }

    public ServiceLimitException(String message) {
        this.desc = message;
        this.errorCodeEnum = ErrorCodeEnum.SYSTEM_EXCEPTION;

    }

    public ServiceLimitException(ErrorCodeEnum code) {
        this.errorCodeEnum = code;
        this.desc = code.desc();
    }

    public ServiceLimitException(ErrorCodeEnum code, String desc) {
        this.errorCodeEnum = code;
        this.desc = desc;
    }

    public ServiceLimitException(ErrorCodeEnum code, Throwable cause) {
        this.errorCodeEnum = code;
        this.desc = cause.getMessage();
    }
}
