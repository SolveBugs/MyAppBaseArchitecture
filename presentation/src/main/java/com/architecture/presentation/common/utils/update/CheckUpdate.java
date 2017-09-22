package com.architecture.presentation.common.utils.update;

import android.content.Context;


public class CheckUpdate {

    private static AppUpdate appUpdate;

    /**
     * 检查新版本提示更新以及下载后安装
     */
    public static void checkUpdate(Context context) {
        if (appUpdate != null) {
            appUpdate.checkUpdate(context);
        }
    }

    public static void setAppUpdate(AppUpdate appUpdate) {
        CheckUpdate.appUpdate = appUpdate;
    }
}