package top.zekozhang.demo.temperaturefetcher;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.zekozhang.demo.temperaturefetcher.enums.ErrorCodeEnum;
import top.zekozhang.demo.temperaturefetcher.exception.ServiceException;
import top.zekozhang.demo.temperaturefetcher.service.TemperatureService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * TemperatureService测试用例
 *
 * @author Zhang Yun
 * @date 2021-09-05 16:42
 */
public class TemperatureServiceTest extends TemperatureFetcherApplicationTests {
    @Autowired
    private TemperatureService temperatureService;

    /**
     * 测试正确返回
     */
    @Test
    public void testGetTemperatureSuccess() {
        String province = "江苏";
        String city = "苏州";
        String county = "吴中";

        Assert.assertNotNull(temperatureService.getTemperature(province, city, county).get());
    }

    /**
     * 测试缓存使用
     * 两次查询用时相同的查询条件，第二次查询不走HTTP接口而是直接从缓存返回数据
     * 日志中应仅有一次HTTP省市区及天气查询请求
     */
    @Test
    public void testGetTemperatureCache() {
        String province = "江苏";
        String city = "苏州";
        String county = "吴中";

        Assert.assertNotNull(temperatureService.getTemperature(province, city, county).get());
        Assert.assertNotNull(temperatureService.getTemperature(province, city, county).get());
    }

    /**
     * 测试省份参数错误
     */
    @Test
    public void testGetTemperatureErrorProvince() {
        String province = "江苏省";
        String city = "苏州";
        String county = "吴中";

        try {
            temperatureService.getTemperature(province, city, county).get();
        } catch (ServiceException e) {
            Assert.assertEquals(e.getErrorCodeEnum(), ErrorCodeEnum.GET_PROVINCE_ERROR);
        }
    }

    /**
     * 测试城市参数错误
     */
    @Test
    public void testGetTemperatureErrorCity() {
        String province = "江苏";
        String city = "苏州市";
        String county = "吴中";

        try {
            temperatureService.getTemperature(province, city, county).get();
        } catch (ServiceException e) {
            Assert.assertEquals(e.getErrorCodeEnum(), ErrorCodeEnum.GET_CITY_ERROR);
        }
    }

    /**
     * 测试地区参数错误
     */
    @Test
    public void testGetTemperatureErrorCounty() {
        String province = "江苏";
        String city = "苏州";
        String county = "吴中区";

        try {
            temperatureService.getTemperature(province, city, county).get();
        } catch (ServiceException e) {
            Assert.assertEquals(e.getErrorCodeEnum(), ErrorCodeEnum.GET_COUNTY_ERROR);
        }
    }

    /**
     * 测试TPS超限
     *
     * @throws InterruptedException InterruptedException
     * @throws ExecutionException   ExecutionException
     */
    @Test
    public void testGetTemperatureTPSLimit() throws InterruptedException, ExecutionException {
        String province = "江苏";
        String city = "苏州";
        String county = "吴中";

        final int threadCount = 101;
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("gettemp-thread-%d").build();
        ExecutorService pool = new ThreadPoolExecutor(threadCount, threadCount, 0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(100), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        // 使用多线程模拟1秒内超过TPS限制的请求
        List<Callable<Integer>> allTasks = new ArrayList<>(threadCount);
        for (int i = 0; i < threadCount; i++) {
            allTasks.add((Callable) () -> temperatureService.getTemperature(province, city, county).get());
        }
        List<Future<Integer>> futureList = pool.invokeAll(allTasks);
        try {
            for (Future<Integer> future : futureList) {
                future.get();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw e;
        }

        pool.shutdown();
        while (!pool.isTerminated()) {
            System.out.println("执行尚未结束...");
        }

    }
}
