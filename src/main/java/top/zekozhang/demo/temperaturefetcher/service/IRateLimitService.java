package top.zekozhang.demo.temperaturefetcher.service;

/**
 * 限流服务接口
 *
 * @author Zhang Yun
 * @date 2021-09-04 14:14
 */
public interface IRateLimitService {

    /**
     * 尝试获取许可证，获取1个，立即返回非阻塞
     *
     * @return 是否获取成功
     */
    boolean tryAcquire();

    /**
     * 尝试获取多个许可证，立即返回非阻塞
     *
     * @param permits 尝试获取许可证数量
     * @return 是否获取成功
     */
    boolean tryAcquire(int permits);

    /**
     * 阻塞获取许可证，获取1个，若超过timeout未获取到许可证
     *
     * @param timeout 获取许可证超时时间
     * @return 是否获取成功
     */
    boolean acquire(long timeout);

    /**
     * 阻塞获取多个许可证，若超过timeout未获取到许可证
     *
     * @param permits 尝试获取许可证数量
     * @param timeout 获取许可证超时时间
     * @return 是否获取成功
     */
    boolean acquire(int permits, long timeout);

}
