package com.haier.core.util;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/17 14:40
 */
public class HttpUtils {
    private static final OkHttpClient client =
            new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)  // 设置超时时间
//                    .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getTrustManager())
//                    .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                    .build();


    public static final MediaType MEDIA_TYPE_JSON = MediaType.get("application/json; charset=utf-8");


    /**
     * 发起Get请求
     *
     * @param url    url
     * @param params 参数
     * @return 响应结果
     */
    public static String doGet(String url, Map<String, Object> params) {
        Call call = createGetCall(url, params);
        return execute(call);
    }

    /**
     * 发起 Post请求, 使用form表单参数
     *
     * @param url    url
     * @param params 参数
     * @return 响应结果
     */
    public static String doPost(String url, Map<String, Object> params) {
        Call call = createPostCall(url, params);
        return execute(call);
    }

    /**
     * 发起 post请求, 使用json参数
     *
     * @param url  url
     * @param json json参数
     * @return resp
     */
    public static String doPost(String url, String json) {
        Call call = createPostJsonCall(url, json);
        return execute(call);
    }


    /**
     * [异步] 发起Get请求
     *
     * @param url      url
     * @param params   参数
     * @param callback 回调方法
     */
    public static void doGetAsync(String url, Map<String, Object> params, Callback callback) {
        Call call = createGetCall(url, params);
        call.enqueue(callback);
    }

    /**
     * [异步] 发起 Post请求
     *
     * @param url    url
     * @param params 参数
     */
    public static void doPostAsync(String url, Map<String, Object> params, Callback callback) {
        Call call = createPostCall(url, params);
        call.enqueue(callback);
    }

    /**
     * [异步] 发起 post请求, 使用json参数
     *
     * @param url  url
     * @param json json参数
     */
    public static void doPostAsync(String url, String json, Callback callback) {
        Call call = createPostJsonCall(url, json);
        call.enqueue(callback);
    }


    /**
     * Get请求, 构造 Call对象
     *
     * @param url    请求url
     * @param params 请求参数
     * @return Call
     */
    private static Call createGetCall(String url, Map<String, Object> params) {

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();
        // 设置参数
        HttpUrl httpUrl = createHttpUrl(request, params);
        builder.url(httpUrl).build();

        return client.newCall(builder.build());
    }

    /**
     * Post请求, 构造 Call对象
     *
     * @param url    请求url
     * @param params 请求参数
     * @return Call
     */
    private static Call createPostCall(String url, Map<String, Object> params) {
        Request request = new Request.Builder()
                .post(createFormBody(params))
                .url(url)
                .build();
        return client.newCall(request);
    }

    /**
     * Post请求, 构造 Call对象
     *
     * @param url  请求url
     * @param json 请求参数
     * @return Call
     */
    private static Call createPostJsonCall(String url, String json) {
        Request request = new Request.Builder()
                .post(RequestBody.create(MEDIA_TYPE_JSON, json))
                .url(url)
                .build();
        return client.newCall(request);
    }


    /**
     * 为 get请求设置参数
     *
     * @param request request对象
     * @param params  请求参数
     * @return 设置了参数的 HttpUrl对象
     */
    private static HttpUrl createHttpUrl(Request request, Map<String, Object> params) {
        HttpUrl.Builder urlBuilder = request.url().newBuilder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return urlBuilder.build();
    }


    /**
     * 为 post请求设置参数
     *
     * @param params 请求参数
     * @return FormBody
     */
    private static FormBody createFormBody(Map<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return builder.build();
    }


    /**
     * 同步执行 http请求
     *
     * @param call call对象
     * @return 响应结果
     */
    public static String execute(Call call) {
        String respStr = "";
        try {
            ResponseBody body = call.execute().body();
            if (body != null) {
                respStr = body.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respStr;
    }
}
