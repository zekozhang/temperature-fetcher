package top.zekozhang.demo.temperaturefetcher.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.zekozhang.demo.temperaturefetcher.interceptor.OkHttpRetryInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * HTTP请求
 *
 * @author Zhang Yun
 * @date 2021-09-04 15:23
 */
@Slf4j
public class HttpUtils {
    private static final OkHttpClient client;
    /**
     * 兼容的HttpClient
     * OKHttp3默认支持TSL协议1.2和1.3
     * 兼容的HttpClient支持TLS1, 1.1, 1.2, 1.3
     * 如果Server端支持的TSL协议较老可以使用兼容Client调用
     */
    private static final OkHttpClient compatibleClient;

    static {
        OkHttpRetryInterceptor okHttpRetryInterceptor = new OkHttpRetryInterceptor.Builder()
                .executionCount(3)
                .retryInterval(1000)
                .build();

        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(okHttpRetryInterceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS).build();


        compatibleClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(okHttpRetryInterceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectionSpecs(ImmutableList.of(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT)).build();
    }


    /**
     * 处理Get请求
     *
     * @param url 请求地址
     * @return 响应JSON
     * @throws IOException IO异常
     */
    public static JSONObject get(String url) throws IOException {
        JSONObject respjson = new JSONObject();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            respjson.put("message", com.alibaba.fastjson.JSONObject.parseObject(response.body().string()));
            log.info("success:url===" + url + " | response==="
                    + respjson.getJSONObject("message"));
        } else {
            log.info("error:url===" + url + " | Unexpected code " + response);
            throw new IOException("Unexpected code " + response);
        }
        return respjson;
    }
}
