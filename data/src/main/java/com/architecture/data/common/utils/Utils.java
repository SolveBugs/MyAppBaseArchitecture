package com.architecture.data.common.utils;

import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;


public class Utils {
    private static long lastClickTime;
    private final static int SPACE_TIME = 400;
    private final static String COMMAND_LOG_FILE = "command.txt";
    private final static String DNURSE_LOG_FILE = "allinonelog.txt";
    private static final String ENCODE = "UTF-8";

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId();
        if (TextUtils.isEmpty(IMEI)) {
            IMEI = getMac(context);
        }
        return IMEI;
    }

    public static String getMac(Context context) {
        android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        String mac = wifi.getConnectionInfo().getMacAddress();
        return mac;
    }

    public synchronized static boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        boolean isClick2;
        if (currentTime - lastClickTime > SPACE_TIME) {
            lastClickTime = currentTime;
            isClick2 = false;
        } else {
            isClick2 = true;
        }
        return isClick2;
    }

    public static String getSDCardPath() {
        File sdcardDir = null;
        boolean sdcardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
            return sdcardDir.toString();
        } else {
            return null;
        }
    }

    public static void writeCommandLogSd(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        synchronized (str) {
            String sdFileName = getSDCardPath() + "/" + COMMAND_LOG_FILE;
            writeToSd(str, sdFileName);
        }
    }

    public static void writeToSd(String str, String sdFileName) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        File SDFile = new File(sdFileName);
        BufferedWriter writer = null;
        try {
            StringBuilder sbContent = new StringBuilder();
            sbContent.append("  [").append(new Date().toLocaleString()).append("." + (System.currentTimeMillis() % 1000) + "]  ");
            sbContent.append(str);

            if (SDFile.exists()) {
                FileInputStream inputStream = new FileInputStream(SDFile);
                if (inputStream.available() > 1048576) {
                    SDFile.delete();
                }
            }
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(SDFile, true), ENCODE);
            writer = new BufferedWriter(outputStreamWriter);
            writer.write(sbContent.toString() + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeToSd(String str) {

        if (TextUtils.isEmpty(str)) {
            return;
        }
        synchronized (str) {
            String sdFileName = getSDCardPath() + "/" + DNURSE_LOG_FILE;
            writeToSd(str, sdFileName);
        }
    }
}
