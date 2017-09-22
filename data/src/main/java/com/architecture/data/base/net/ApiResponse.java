package com.architecture.data.base.net;

import com.google.gson.annotations.SerializedName;


public class ApiResponse<T> {
    @SerializedName("s")
    private int statusCode;
    @SerializedName("m")
    private String message;
    @SerializedName("d")
    private T data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
