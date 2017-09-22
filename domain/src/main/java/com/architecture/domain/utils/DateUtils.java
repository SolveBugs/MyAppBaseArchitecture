/*
 * Copyright (c) 2014. Beijing Dnurse Technology Ltd. All rights reserved.
 */

package com.architecture.domain.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateUtils {

    private static final String TAG = "DateUtils";
    public final static String DATE_FORMAT_yyyyMMdd_DIVIDE_CROSS = "yyyy-MM-dd";
    public final static String yyyyMMddHHmmGAP = "yyyy-MM-dd HH:mm";
    public final static long MILLIS_ON_MINUTE = 60000;
    public final static long MILLIS_ONE_HOUR = MILLIS_ON_MINUTE * 60;
    public final static long MILLIS_ONE_DAY = MILLIS_ONE_HOUR * 24;
    public final static String DATE_FORMAT_MM = "M";

    //获得某一时间距离零点的毫秒数
    public static long getDateDistanceZero(Date date) {
        return date.getTime() - getDateZero(date).getTime();
    }

    //获得某天时间的零点
    public static Date getDateZero(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Calendar YYYYMMDDString2Calendar(String str) {
        DateFormat format = new SimpleDateFormat(DATE_FORMAT_yyyyMMdd_DIVIDE_CROSS, Locale.US);
        if (!isEmpty(str)) {
            Date date = null;
            try {
                date = format.parse(str);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar;
            } catch (ParseException e) {
                Logger.e(TAG, e.toString());
            }
        }
        return null;
    }

    private static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }


    /**
     * 根据传入的时间和格式，格式化一个时间. format可以选择本类中的静态变量
     * 后缀含义：GAP:用“-”隔开；CHN:中国化，年月日；SLASH：用“/”分开; COLON：用“：”分开
     *
     * @param date,format
     * @return formatString 返回格式化后的日期字符串
     * @author wdd
     * @date 2014.12.23
     */
    public static String formatDate(Date date, String format) {
        if (date != null && format != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        }
        return "";
    }

    public static String formatDate(long date, String format) {
        return formatDate(new Date(date), format);
    }

    public static long getSomeDayEndTime(long mills) {
        return getDate24(mills).getTime();

    }

    //获得某一时间的24点
    public static Date getDate24(long milli) {
        Date date = new Date(milli);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        return calendar.getTime();
    }

    public static long getSomeDayStartTime(long mills) {
        return getDateZero(mills).getTime();
    }

    //获得某天时间的零点
    public static Date getDateZero(long date) {
        return getDateZero(new Date(date));
    }
}