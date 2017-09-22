/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * <p>
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.architecture.data.common.net;

import android.support.annotation.NonNull;


import com.architecture.data.BuildConfig;
import com.architecture.data.common.utils.Utils;
import com.architecture.data.exception.NetException;
import com.architecture.domain.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@Singleton
public class ApiConnectionImp implements ApiConnection {
    private static final String TAG = "ApiConnectionImp";

    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";
    private static final int TIMEOUT = 10000;

    private final OkHttpClient okHttpClient;

    @Inject
    public ApiConnectionImp() {
        okHttpClient = new OkHttpClient();

        // 设置超时时间
        okHttpClient
                .newBuilder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public String doSyncGet(@NonNull String url) throws NetException {
        return doSyncGet(url, null);
    }

    @Override
    public String doSyncGet(@NonNull String url, Map<String, String> params) throws NetException {
        String requestUrl = url;
        Utils.writeToSd("网络请求->URL:"+url+"\n参数："+params);
        if (params != null && params.size() > 0) {
            StringBuilder requestParam = new StringBuilder();

            int pos = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (pos > 0) {
                    requestParam.append("&");
                }

                try {
                    String value = URLEncoder.encode(entry.getValue(), "utf-8");
                    requestParam.append(String.format("%s=%s", entry.getKey(), value));
                } catch (UnsupportedEncodingException e) {
                    Logger.e(
                            TAG,
                            "doGet encode error: url="
                                    + url
                                    + ", param: "
                                    + entry.getKey()
                                    + ", value:"
                                    + entry.getValue());
                }

                pos++;
            }

            requestParam.append(String.format("&time=%d", System.currentTimeMillis()/1000));
            if (url.contains("?")) {
                requestUrl = url + "&" + requestParam.toString();
            } else {
                requestUrl = url + "?" + requestParam.toString();
            }
        }

        if (BuildConfig.DEBUG) {
            Logger.d(TAG, "doSyncGet: url:" + requestUrl);
        }

        try {
            final Request request = getRequest().url(new URL(requestUrl)).get().build();

            return okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            throw new NetException(e);
        }
    }

    @Override
    public String doSyncGet(@NonNull String url, Map<String, String> params, String signatureKey) throws NetException {
        String requestUrl = url;
        Utils.writeToSd("网络请求->URL:"+url+"\n参数："+params);
        if (params != null && params.size() > 0) {
            StringBuilder requestParam = new StringBuilder();

            int pos = 0;
            String dataStr = "";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (pos > 0) {
                    requestParam.append("&");
                }

                try {
                    String value = entry.getValue();
                    dataStr += value;
                    value = URLEncoder.encode(entry.getValue(), "utf-8");
                    requestParam.append(String.format("%s=%s", entry.getKey(), value));
                } catch (UnsupportedEncodingException e) {
                    Logger.e(
                            TAG,
                            "doGet encode error: url="
                                    + url
                                    + ", param: "
                                    + entry.getKey()
                                    + ", value:"
                                    + entry.getValue());
                }

                pos++;
            }

            requestParam.append(String.format("&sign=%s", MD5(dataStr + signatureKey)));
            requestParam.append(String.format("&time=%d", System.currentTimeMillis()/1000));

            if (url.contains("?")) {
                requestUrl = url + "&" + requestParam.toString();
            } else {
                requestUrl = url + "?" + requestParam.toString();
            }
        }

        if (BuildConfig.DEBUG) {
            Logger.d(TAG, "doSyncGet: url:" + requestUrl);
        }

        try {
            final Request request = getRequest().url(new URL(requestUrl)).get().build();

            return okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            throw new NetException(e.getLocalizedMessage());
        }
    }

    private Request.Builder getRequest() {
        return new Request.Builder().addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON).get();
    }

    @Override
    public String doSyncPost(@NonNull String url, Map<String, String> params) throws NetException {
        return doSyncPost(url, params, null, null);
    }

    @Override
    public String doSyncPost(
            @NonNull String url, Map<String, String> params, Map<String, File> files)
            throws NetException {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Utils.writeToSd("网络请求->URL:"+url+"\n参数："+params+"\n文件"+files);

        if (params != null && params.size() > 0) {
            String dataStr = "";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
                dataStr += entry.getValue();
            }
        }

        if (files != null && files.size() > 0) {
            for (Map.Entry<String, File> entry : files.entrySet()) {
                File uploadFile = entry.getValue();
                if (!uploadFile.exists()) {
                    Logger.e(TAG, "doPost: file not exist:" + uploadFile.getPath());
                }

                RequestBody fileBody =
                        RequestBody.create(
                                MediaType.parse("application/octet-stream"), entry.getValue());
                builder.addFormDataPart(entry.getKey(), uploadFile.getName(), fileBody);
            }
        }

        builder.addFormDataPart("time", String.valueOf(System.currentTimeMillis()/1000));

        try {
            final Request request = getRequest().url(new URL(url)).post(builder.build()).build();

            return okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            throw new NetException(e.getLocalizedMessage());
        }
    }

    @Override
    public String doSyncPost(
            @NonNull String url, Map<String, String> params, Map<String, File> files, String signatureKey)
            throws NetException {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Logger.d(TAG, "URL:"+url+"\n参数："+params+"\n文件"+files);
        Utils.writeToSd("网络请求->URL:"+url+"\n参数："+params+"\n文件"+files);

        if (params != null && params.size() > 0) {
            String dataStr = "";
            for (Map.Entry<String, String> entry : params.entrySet()) {
                dataStr += entry.getValue();
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }

        if (files != null && files.size() > 0) {
            for (Map.Entry<String, File> entry : files.entrySet()) {
                File uploadFile = entry.getValue();
                if (!uploadFile.exists()) {
                    Logger.e(TAG, "doPost: file not exist:" + uploadFile.getPath());
                }

                RequestBody fileBody =
                        RequestBody.create(
                                MediaType.parse("application/octet-stream"), entry.getValue());
                builder.addFormDataPart(entry.getKey(), uploadFile.getName(), fileBody);
            }
        }

        builder.addFormDataPart("time", String.valueOf(System.currentTimeMillis()/1000));

        try {
            final Request request = getRequest().url(new URL(url)).post(builder.build()).build();

            return okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            throw new NetException(e);
        }
    }

    public static String MD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 得到存放哈希值结果的byte数组
            return byteToString(md.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    // 全局数组
    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }
}
