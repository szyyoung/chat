package com.example.client.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpUtil {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private static final OkHttpClient httpClient = new OkHttpClient()
            .newBuilder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(6, TimeUnit.SECONDS)
            .writeTimeout(6, TimeUnit.SECONDS)
            .build();


    public static JSONObject get(String url, Map<String, String> urlParams, Map<String, String> headers) {
        url = buildUrl(url, urlParams);

        Request.Builder request = new Request.Builder()
                .url(url);

        request = buildHeader(request, headers);

        try (Response response = httpClient.newCall(request.build()).execute()) {
            String string = response.body().string();
            if (StringUtils.isEmpty(string)) return null;
            return JSONObject.parseObject(string);
        } catch (IOException e) {
            log.info("HttpUtil#Get err:", e);
        }
        return null;
    }


    public static JSONObject post(String url, Map<String, String> headers, Map<String, Object> params) {
        RequestBody body = RequestBody.create(JSONObject.toJSONString(params), JSON);
        Request.Builder request = new Request.Builder();
        request = buildHeader(request, headers);
        request.url(url)
                .post(body)
                .build();
        try (Response response = httpClient.newCall(request.build()).execute()) {
            String string = response.body().string();
            if (StringUtils.isEmpty(string)) return null;
            return JSONObject.parseObject(string);
        } catch (IOException e) {
            log.info("HttpUtil#Post err:", e);
        }
        return null;
    }


    private static String buildUrl(String url, Map<String, String> urlParams) {
        if (urlParams != null && urlParams.size() > 0) {
            StringBuilder builder = new StringBuilder(url).append("?");
            for (String key : urlParams.keySet()) {
                String value = urlParams.get(key);
                builder.append(key).append("=").append(value).append("&");
            }
            url = builder.toString().substring(0, builder.toString().length() - 1);
        }
        return url;
    }

    private static Request.Builder buildHeader(Request.Builder request, Map<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            headers.keySet().forEach(key -> {
                request.addHeader(key, headers.get(key));
            });
        }
        //添加token
        request.addHeader(Constant.TOKEN, String.valueOf(Constant.HOLDER.get(Constant.TOKEN)));
        return request;
    }


    private HttpUtil() {

    }
}
