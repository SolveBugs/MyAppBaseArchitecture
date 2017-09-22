package com.architecture.data.common.net;

import android.support.annotation.NonNull;


import com.architecture.data.exception.NetException;

import java.io.File;
import java.util.Map;


/**
 * 网络接口 目前只支持同步操作
 */
public interface ApiConnection {
    /**
     * 简化处理 参数在地址上
     *
     * @param url 请求地址
     * @return 网络返回的内容
     */
    String doSyncGet(@NonNull String url) throws NetException;

    /**
     * 处理get请求 url 可以以?ab=1的形式传递参数，也可能用过params全部参数
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 网络返回的内容
     */
    String doSyncGet(@NonNull String url, Map<String, String> params) throws NetException;

    /**
     * 处理get请求 url 可以以?ab=1的形式传递参数，也可能用过params全部参数
     *
     * @param url    请求地址
     * @param params 请求参数
     * @param signatureKey 签名的key，传null为默认key
     * @return 网络返回的内容
     */
    String doSyncGet(@NonNull String url, Map<String, String> params, String signatureKey) throws NetException;

    /**
     * 处理post请求 只处理参数
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 网络返回的内容
     */
    String doSyncPost(@NonNull String url, Map<String, String> params) throws NetException;

    /**
     * 处理post请求 同时处理参数和文件
     *
     * @param url    请求地址
     * @param params 请求参数
     * @param files  上传的文件
     * @return 网络返回的内容
     */
    String doSyncPost(@NonNull String url, Map<String, String> params, Map<String, File> files)
            throws NetException;

    /**
     * 处理post请求 同时处理参数和文件
     *
     * @param url    请求地址
     * @param params 请求参数
     * @param files  上传的文件
     * @param signatureKey 签名的key，传null为默认key
     * @return 网络返回的内容
     */
    String doSyncPost(@NonNull String url, Map<String, String> params, Map<String, File> files, String signatureKey)
            throws NetException;
}
