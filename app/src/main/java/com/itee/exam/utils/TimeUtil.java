package com.itee.exam.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xin on 2015-06-17.
 */
public class TimeUtil {

    /**
     * 格式化时间（输出类似于 刚刚, 4分钟前, 一小时前, 昨天这样的时间）
     *
     * @param tDate 需要格式化的时间 如"2014-07-14 19:01:45"
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static String formatDisplayTime(Date tDate) {
        String display = "";
        int tMin = 60 * 1000;
        int tHour = 60 * tMin;
        int tDay = 24 * tHour;
        try {
            Date today = new Date();
            SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy");
            SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd");
            Date thisYear = new Date(thisYearDf.parse(thisYearDf.format(today)).getTime());
            Date yesterday = new Date(todayDf.parse(todayDf.format(today)).getTime());
            Date beforeYes = new Date(yesterday.getTime() - tDay);
            if (tDate != null) {
                SimpleDateFormat halfDf = new SimpleDateFormat("MM月dd日");
                long dTime = today.getTime() - tDate.getTime();
                if (tDate.before(thisYear)) {
                    display = new SimpleDateFormat("yyyy年MM月dd日").format(tDate);
                } else {
                    if (dTime < tMin) {
                        display = "刚刚";
                    } else if (dTime < tHour) {
                        display = (int) Math.ceil(dTime / tMin) + "分钟前";
                    } else if (dTime < tDay && tDate.after(yesterday)) {
                        display = (int) Math.ceil(dTime / tHour) + "小时前";
                    } else if (tDate.after(beforeYes) && tDate.before(yesterday)) {
                        display = "昨天" + new SimpleDateFormat("HH:mm").format(tDate);
                    } else {
                        display = halfDf.format(tDate);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return display;
    }
}
