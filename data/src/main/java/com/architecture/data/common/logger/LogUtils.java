package com.architecture.data.common.logger;

import android.content.Context;

import com.architecture.domain.utils.Logger;

public class LogUtils {
    private static boolean sHasInit = false;

    public static void init(Context context) {
        if (!sHasInit) {
            sHasInit = true;

            LogWrapper logWrapper = new LogWrapper();
            Logger.setLogNode(logWrapper);
        }
    }
}
