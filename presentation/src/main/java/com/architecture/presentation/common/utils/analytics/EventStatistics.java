package com.architecture.presentation.common.utils.analytics;

import android.content.Context;


public class EventStatistics {
    private static Analytics analytics;

    public static void onEvent(Context context, String event) {
        if (analytics != null) {
            analytics.onEvent(context, event);
        }
    }

    public static void onPageStart(String pageName) {
        if (analytics != null) {
            analytics.onPageStart(pageName);
        }

    }

    public static void onPageEnd(String pageName) {
        if (analytics != null) {
            analytics.onPageEnd(pageName);
        }
    }

    public static void setDebugMode(boolean debugMode) {
        if (analytics != null) {
            analytics.setDebugMode(debugMode);
        }
    }

    public static void onPause(Context context) {
        if (analytics != null) {
            analytics.onPause(context);
        }
    }

    public static void onResume(Context context) {
        if (analytics != null) {
            analytics.onResume(context);
        }
    }

    public static void setAnalytics(Analytics newAnalytics) {
        analytics = newAnalytics;
    }
}