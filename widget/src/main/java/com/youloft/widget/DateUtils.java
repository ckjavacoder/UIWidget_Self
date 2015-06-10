package com.youloft.widget;

import java.util.Calendar;

/**
 * Created by javen on 15/6/9.
 */
public class DateUtils {
    public static final int EPOCH_JULIAN_DAY = 2440588;

    /**
     * 获取相差多少天
     *
     * @param cal
     * @param cal1
     * @return
     */
    public static final int getDayOffset(Calendar cal, Calendar cal1) {
        return getJuliDay(cal) - getJuliDay(cal1);

    }


    /**
     * 以天为单位获取
     *
     * @param cal
     * @return
     */
    public static final int getJuliDay(Calendar cal) {
        long offsetMillis = (cal.get(Calendar.DST_OFFSET) + cal.get(Calendar.ZONE_OFFSET)) * 1000;
        long juliDay = (cal.getTimeInMillis() + offsetMillis) / android.text.format.DateUtils.DAY_IN_MILLIS;
        return (int) (juliDay + EPOCH_JULIAN_DAY);
    }

    /**
     * 以当前日期为基准获取一共有多少周
     *
     * @param baseDate
     * @return
     */
    public static int getWeeks(Calendar baseDate) {
        return baseDate.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 是否为同一月
     *
     * @param date
     * @param date1
     * @return
     */
    public static boolean isSameMonth(Calendar date, Calendar date1) {
        return date.get(Calendar.YEAR) == date1.get(Calendar.YEAR)
                && date.get(Calendar.MONTH) == date1.get(Calendar.MONTH);
    }
}
