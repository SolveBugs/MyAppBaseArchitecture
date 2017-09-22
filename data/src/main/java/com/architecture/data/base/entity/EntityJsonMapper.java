package com.architecture.data.common.entity;

import com.architecture.data.base.net.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 将网络返回的数据转成实体类
 *
 * @param <T> data 结中的类型
 */
public class EntityJsonMapper<T> {
    private final Gson gson;

    public EntityJsonMapper() {
        this.gson = new Gson();
    }

    /**
     * @param json json 字符串
     * @param type 对象类型信息
     * @return 服务返回类型
     */
    public ApiResponse<T> transformEntity(String json, TypeToken type) {
        return this.gson.fromJson(json, type.getType());
    }

    /**
     * @param param T 字符串
     * @return 字符串
     */
    public String toJson(T param) {
        return this.gson.toJson(param, param.getClass());
    }
}
