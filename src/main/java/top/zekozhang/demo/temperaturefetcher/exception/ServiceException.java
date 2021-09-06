package top.zekozhang.demo.temperaturefetcher.exception;


import lombok.Getter;
import top.zekozhang.demo.temperaturefetcher.enums.ErrorCodeEnum;

/**
 * 业务逻辑服务类异常
 *
 * @author Zhang Yun
 * @date 2021-09-04 15:20
 */
@Getter
public class ServiceException extends RuntimeException {

    /**
     * 异常代码
     */
    private final ErrorCodeEnum errorCodeEnum;

    /**
     * 异常说明
     */
    private final String desc;

    public ServiceException() {
        super();
        this.errorCodeEnum = ErrorCodeEnum.SYSTEM_EXCEPTION;
        this.desc = ErrorCodeEnum.SYSTEM_EXCEPTION.desc();
    }

    public ServiceException(String message) {
        super(message);
        this.desc = message;
        this.errorCodeEnum = ErrorCodeEnum.SYSTEM_EXCEPTION;
    }

    public ServiceException(ErrorCodeEnum code) {
        super(code.desc());
        this.errorCodeEnum = code;
        this.desc = code.desc();
    }

    public ServiceException(ErrorCodeEnum code, String desc) {
        super(desc);
        this.errorCodeEnum = code;
        this.desc = desc;
    }

    public ServiceException(ErrorCodeEnum code, String desc, Throwable cause) {
        super(cause);
        this.errorCodeEnum = code;
        this.desc = desc;
    }

    public ServiceException(ErrorCodeEnum code, String desc, String message) {
        super(message);
        this.errorCodeEnum = code;
        this.desc = desc;
    }
}
