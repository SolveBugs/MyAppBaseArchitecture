package com.architecture.data.common.net;


/**
 * 基类定义通用地址
 */
public class ApiBase {
    /**
     * 正式服务器地址, 在gradle中配置
     */
    protected static final String HOST_RUL = "https://github.com/";

    /**
     * 测试服务器地址, 在gradle中配置
     */
    protected static final String HOST_DEV_RUL = "https://github.com/";

    /**
     * 返回当前使用的服务器址，处理地址切换
     *
     * @return 当前地址
     */
    public String getHost() {
        return HOST_RUL;
    }

    /**
     * 返回完整地址
     *
     * @param path 请求子路径
     * @return 完整地址
     */
    public String getFullUrl(String path) {
        return getHost() + path;
//        return "http://192.168.1.83:8083/api/" + path;
    }
}
