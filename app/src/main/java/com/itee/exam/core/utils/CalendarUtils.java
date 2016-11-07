package com.itee.exam.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {

    /**
     * 日期格式化
     */
    public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 年月格式化
     */
    public static final SimpleDateFormat FORMAT_YEAR_MONTH = new SimpleDateFormat("yyyy-MM");

    /**
     * 月日格式化
     */
    public static final SimpleDateFormat FORMAT_MONTH_DAY = new SimpleDateFormat("MM-dd");

    public static final SimpleDateFormat FORMAT_AAA_TIME_NOT_SECOND = new SimpleDateFormat("aaa HH:mm");

    /**
     * 时间格式化(不含秒)
     */
    public static final SimpleDateFormat FORMAT_TIME_NOT_SECOND = new SimpleDateFormat("HH:mm");

    /**
     * 时间格式化
     */
    public static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("HH:mm:ss");

    /**
     * 日期时间格式化(不含秒)
     */
    public static final SimpleDateFormat FORMAT_DATE_TIME_NOT_SECOND = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 日期格式化字符串
     */
    public static final SimpleDateFormat FORMAT_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 将时间格式化日期格式(yyyy-MM-dd)
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return formatDateByFormat(date, FORMAT_DATE);
    }

    public static String formatMonthDay(Date date) {
        return formatDateByFormat(date, FORMAT_MONTH_DAY);
    }

    /**
     * 将时间格式为时间形式(HH:mm:ss)
     *
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
        return formatDateByFormat(date, FORMAT_TIME);
    }

    /**
     * 将时间格式化为不带秒的时间形式(HH:mm)
     *
     * @param date
     * @return
     */
    public static String formatTimeNoSecond(Date date) {
        return formatDateByFormat(date, FORMAT_TIME_NOT_SECOND);
    }

    public static String formatAaaTimeNoSecond(Date date) {
        return formatDateByFormat(date, FORMAT_AAA_TIME_NOT_SECOND);
    }

    /**
     * 将时间格式化为日期时间格式(yyyy-MM-dd HH:mm:ss)
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        return formatDateByFormat(date, FORMAT_DATE_TIME);
    }

    /**
     * 将时间格式化为日期时间格式(MM-dd)
     *
     * @param date
     * @return
     */
    public static String formatMonthAndDay(Date date) {
        return formatDateByFormat(date, FORMAT_MONTH_DAY);
    }

    /**
     * 将时间格式化为日期时间格式不带秒(yyyy-MM-dd HH:mm)
     *
     * @param date
     * @return
     */
    public static String formatDateTimeNotSecond(Date date) {
        return formatDateByFormat(date, FORMAT_DATE_TIME_NOT_SECOND);
    }

    /**
     * 将时间格式化为指定格式
     *
     * @param date
     * @return
     */
    public final static String formatDateByFormat(Date date, SimpleDateFormat format) {
        String result = "";
        if (date != null) {
            result = format.format(date);
        }
        return result;
    }

    /**
     * 将java.util.Date转化为java.sql.Date
     *
     * @param date
     * @return
     */
    public static java.sql.Date parseSqlDate(Date date) {
        if (date == null) {
            throw new NullPointerException();
        }
        return new java.sql.Date(date.getTime());
    }

    /**
     * 获取年份
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取月份
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日期(一个月中的哪一天)
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取小时(24小时制)
     *
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取分钟
     *
     * @param date
     * @return
     */
    public static int getMinute(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    /**
     * 获取秒
     *
     * @param date
     * @return
     */
    public static int getSecond(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.SECOND);
    }

    /**
     * 获取毫秒
     *
     * @param date
     * @return
     */
    public static long getMillis(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    /**
     * 获取指定时间是星期几
     *
     * @param date
     * @return
     */
    public static int getWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = dayOfWeek - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        return dayOfWeek;
    }

    /**
     * 日期相加
     *
     * @param date Date
     * @param day  int
     * @return Date
     */
    public static Date addDate(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
        return c.getTime();
    }

    /**
     * 日期相减
     *
     * @param date  Date
     * @param date1 Date
     * @return int
     */
    public static int diffDate(Date date, Date date1) {
        return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
    }

    /**
     * 将字符串转换为日期格式的Date类型(yyyy-MM-dd)
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException {
        return FORMAT_DATE.parse(date);
    }

    /**
     * 将字符串转换为日期时间格式的Date类型(yyyy-MM-dd HH:mm:ss)
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDateTime(String date) throws ParseException {
        return FORMAT_DATE_TIME.parse(date);
    }

    /**
     * 将字符串转换为日期时间格式的Date类型(yyyy-MM-dd HH:mm)
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDateTimeNoSecond(String date) throws ParseException {
        return FORMAT_DATE_TIME_NOT_SECOND.parse(date);
    }

    public static boolean compareDateAndWeekDay(Date date, int weekDay) {
        return weekDay == getWeek(date);
    }

    /**
     * 将字符串转换为日期时间格式的Date类型( MM-dd )
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseMonthDay(String date) throws ParseException {
        return FORMAT_MONTH_DAY.parse(date);
    }

    /**
     * 将字符串转为时间格式的Date类型(HH:mm)
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseTimeNoSecond(String date) throws ParseException {
        return FORMAT_TIME_NOT_SECOND.parse(date);
    }

    /**
     * 将字符串转为时间格式的Date类型(HH:mm:ss)
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseTimeNoDate(String date) throws ParseException {
        return FORMAT_TIME.parse(date);
    }

    /**
     * 获得当前时间
     *
     * @return
     */
    public static String getCurrentDateTime() {
        Date date = new Date();
        return formatDateTime(date);
    }

    /**
     * 时间比较yyyy-MM-dd
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean compareDate(Date date1, Date date2) {
        String str1 = formatDate(date1);
        String str2 = formatDate(date2);
        return str1.equals(str2);
    }

    /**
     * 时间比较HH:mm
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean compareDateTimeHHmm(Date date1, Date date2) {
        String str1 = formatTimeNoSecond(date1);
        String str2 = formatTimeNoSecond(date2);
        return str1.equals(str2);
    }

    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    public static Date getFormatDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        int year1 = cal1.get(Calendar.YEAR);
        int month1 = cal1.get(Calendar.MONTH);
        int day1 = cal1.get(Calendar.DAY_OF_MONTH);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int year2 = cal2.get(Calendar.YEAR);
        int month2 = cal2.get(Calendar.MONTH);
        int day2 = cal2.get(Calendar.DAY_OF_MONTH);
        return year1 == year2 && month1 == month2 && day1 == day2;
    }

    public static int daysBetween(Date date1, Date date2) {
        if (date1 == null || date2 == null) return 0;
        long qua = millisBetween(date1, date2);
        return (int) (qua / (60 * 60 * 1000 * 24));
    }

    public static int hoursBetween(Date date1, Date date2) {
        long qua = date1.getTime() - date2.getTime();
        return (int) Math.ceil(qua / (60 * 60 * 1000.0));
    }

    public static long millisBetween(Date date1, Date date2) {
        if (date1 == null || date2 == null) return 0;
        return date1.getTime() - date2.getTime();
    }
}