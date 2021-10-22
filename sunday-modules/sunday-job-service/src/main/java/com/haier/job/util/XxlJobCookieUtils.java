package com.haier.job.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haier.core.util.HttpUtils;
import okhttp3.*;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.job.util
 * @ClassName: XxlJobCookieUtils
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/22 14:28
 * @Version: 1.0
 */
public class XxlJobCookieUtils {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * xxl-job登录cookies处理
     *
     * @param url
     * @param key
     * @param obj
     * @return
     */
    public static List<Cookie> getCookie(String url, String key, Object obj) {
        List<Cookie> result = new ArrayList<>();
        try {
            String tokenJson = objectMapper.writeValueAsString(obj);
            String tokenHex = new BigInteger(tokenJson.getBytes()).toString(16);
            Cookie cookie = new Cookie.Builder().name(key).value(tokenHex).domain(HttpUrl.get(url).host()).build();
            result.add(cookie);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 针对xxl-job-admin的发送请求
     * @param url
     * @param params
     * @param cookies
     * @return
     */
    public static String doGet(String url, Map<String, Object> params, List<Cookie> cookies) {
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();
        // 设置参数
        HttpUrl.Builder urlBuilder = request.url().newBuilder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        HttpUrl httpUrl = urlBuilder.build();
        builder.url(httpUrl).build();
        okHttpClient.cookieJar().saveFromResponse(httpUrl, cookies);
        return HttpUtils.execute(okHttpClient.newCall(builder.build()));
    }

    private static final OkHttpClient okHttpClient;

    static {

        Map<String, List<Cookie>> COOKIE_STORE = new HashMap<>();

        okHttpClient = HttpUtils.COMMON_CLIENT.newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)  // 设置超时时间
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                        COOKIE_STORE.put(httpUrl.host(), list);
                    }

                    @Override
                    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                        List<Cookie> cookies = COOKIE_STORE.get(httpUrl.host());
                        return cookies != null ? cookies : new ArrayList<>();
                    }
                }).build();
    }
}
