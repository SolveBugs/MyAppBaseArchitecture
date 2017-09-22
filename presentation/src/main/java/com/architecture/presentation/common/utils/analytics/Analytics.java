package com.architecture.presentation.common.utils.analytics;

import android.content.Context;



public interface Analytics {
    void onEvent(Context context, String event);

    void onPause(Context context);

    void onResume(Context context);

    void onPageStart(String pageName);

    void onPageEnd(String pageName);

    void setDebugMode(boolean debugMode);
}