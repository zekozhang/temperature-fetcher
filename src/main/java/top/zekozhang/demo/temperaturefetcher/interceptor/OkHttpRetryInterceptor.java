package top.zekozhang.demo.temperaturefetcher.interceptor;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InterruptedIOException;

/**
 * OKHttp重试拦截器
 *
 * @author Zhang Yun
 * @date 2021-09-04 22:59
 */
@Slf4j
public class OkHttpRetryInterceptor implements Interceptor {
    /**
     * 重试的间隔
     */
    private final long retryInterval;
    /**
     * 最大重试次数
     */
    public int executionCount;

    OkHttpRetryInterceptor(Builder builder) {
        this.executionCount = builder.executionCount;
        this.retryInterval = builder.retryInterval;
    }


    /**
     * 重试拦截器实现
     *
     * @param chain 调用链
     * @return 响应
     * @throws IOException IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = doRequest(chain, request);
        int retryNum = 0;
        while ((response == null || !response.isSuccessful()) && retryNum <= executionCount) {
            log.info("HTTP请求失败，重试次数 - {}", retryNum);
            final long nextInterval = getRetryInterval();
            try {
                log.info("等待{}毫秒后重试...", nextInterval);
                Thread.sleep(nextInterval);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InterruptedIOException();
            }
            retryNum++;
            // 请求重试
            response = doRequest(chain, request);
        }
        return response;
    }

    private Response doRequest(Chain chain, Request request) {
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            log.error("HTTP接口" + request.url().url() + "请求失败", e);
        }
        return response;
    }

    public long getRetryInterval() {
        return this.retryInterval;
    }

    public static final class Builder {
        private int executionCount;
        private long retryInterval;

        public Builder() {
            executionCount = 3;
            retryInterval = 1000;
        }

        public OkHttpRetryInterceptor.Builder executionCount(int executionCount) {
            this.executionCount = executionCount;
            return this;
        }

        public OkHttpRetryInterceptor.Builder retryInterval(long retryInterval) {
            this.retryInterval = retryInterval;
            return this;
        }

        public OkHttpRetryInterceptor build() {
            return new OkHttpRetryInterceptor(this);
        }
    }

}