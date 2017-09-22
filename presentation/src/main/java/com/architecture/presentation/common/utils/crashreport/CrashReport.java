package com.architecture.presentation.common.utils.crashreport;

import android.content.Context;


public class CrashReport {

    //崩溃上报

    private static AppCrashReport appCrashReport;

    /**
     * 收集并上报崩溃信息
     */
    public static void crashReport(Context context) {
        if (appCrashReport != null) {
            appCrashReport.reportCrash(context);
        }
    }

    public static void setAppCrashReport(AppCrashReport appCrashReport) {
        CrashReport.appCrashReport = appCrashReport;
    }
}