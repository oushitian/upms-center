package com.jolly.upms.manager.util;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by djb
 * on 2014/11/17.
 */
public class DateUtils {
    public  final static int DAYTIME = 86400;

    /** 年月日 yyyy年MM月dd日 */
    public static final String CHINESE_DTFORMAT     = "yyyy年MM月dd日";
    public static final String FORMAT_DAY_DEFAULT     = "yyyy-MM-dd";
    public static final String FORMAT_TIME_DEFAULT     = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_TIME_WITHOUT_SECONDS     = "yyyy-MM-dd HH:mm";
    public static final String MMddyyyyHHmmss = "MM/dd/yyyy HH:mm:ss";
    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMM = "yyyyMM";


    /**
     * 获取当前时间戳 精确到秒
     *
     * @return
     */
    public static Long getCurrentSecond() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前时间戳 精确到秒
     *
     * @return
     */
    public static Integer getCurrentSecondIntValue() {
        return new Long(System.currentTimeMillis() / 1000).intValue();
    }

    /**
     * 获取当前整点时间戳
     *
     * @return
     */
    public static Long getCurrentIntegralPoint() {
        String formatStr = getCurrentFormatStr("yyyy-MM-dd HH");
        return getSecondFromDateFmtStr(formatStr,"yyyy-MM-dd HH");
    }

    public static Long getSecondForTodayStart() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());
        try {
            Date d = df.parse(date);
            return d.getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static String getCurrentDateStr() {
        return getDateStr(new Date());
    }

    public static String getDateStr(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String timeStr = df.format(date);
        return timeStr;
    }


    public static String getCurrentTimeStr() {
        return getTimeStr(new Date());
    }

    public static String getTimeStr(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH：mm");
        String timeStr = df.format(date);
        return timeStr;
    }

    public static String getCurrentFormatStr(String format) {
        return getFormatStr(new Date(), format);
    }

    public static String getFormatStr(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        String timeStr = df.format(date);
        return timeStr;
    }

    /**
     * 获取格式化后的时间字符串
     *
     * @param second
     * @param format
     * @return
     */
    public static String getFormatDateFromSecond(Long second, String format) {
        if (second == null) {
            return null;
        }
        SimpleDateFormat sFormat = new SimpleDateFormat(format,
                Locale.SIMPLIFIED_CHINESE);
        long milliS = second * 1000l;
        Date date = new Date(milliS);
        return sFormat.format(date);

    }

    /**
     * 获取格式化后的时间字符串
     *
     * @param second
     * @param format
     * @return
     */
    public static String getFormatDateFromSecond(Integer second, String format) {
        if (second == null) {
            return null;
        }

        return getFormatDateFromSecond(second.longValue(), format);
    }

    /**
     * 获取常用格式的时间字符串 年-月-日 时:分:秒
     *
     * @param second
     * @return
     */
    public static String getChineseDateFromSecond(Long second) {
        if (second == null) {
            return null;
        }
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.SIMPLIFIED_CHINESE);
        long milliS = second * 1000l;
        Date date = new Date(milliS);
        return sFormat.format(date);

    }

    public static String getChineseDateFromSecond(Integer second) {
        if (second == null) {
            return null;
        }
        if (second == 0) {
            return "";
        }
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.SIMPLIFIED_CHINESE);
        long milliS = second * 1000l;
        Date date = new Date(milliS);
        return sFormat.format(date);

    }

    public static String getChineseDateFromWithoutSecond(Integer second) {
        if (second == null) {
            return null;
        }
        if (second == 0) {
            return "";
        }
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.SIMPLIFIED_CHINESE);
        long milliS = second * 1000l;
        Date date = new Date(milliS);
        return sFormat.format(date);

    }

    public static String getDateTimeBoxVal(Integer second) {
        if (second == null) {
            return null;
        }
        if (second == 0) {
            return "";
        }
        SimpleDateFormat sFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss",
                Locale.SIMPLIFIED_CHINESE);
        long milliS = second * 1000l;
        Date date = new Date(milliS);
        return sFormat.format(date);

    }

    public static String getFileDateFromSecond(Long second) {
        if (second == null) {
            return null;
        }
        if (second == 0) {
            return "";
        }
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH：mm",
                Locale.SIMPLIFIED_CHINESE);
        long milliS = second * 1000l;
        Date date = new Date(milliS);
        return sFormat.format(date);

    }

    public static String getFileDateFromSecond(Integer second) {
        if (second == null) {
            return null;
        }
        if (second == 0) {
            return "";
        }
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH：mm",
                Locale.SIMPLIFIED_CHINESE);
        long milliS = second * 1000l;
        Date date = new Date(milliS);
        return sFormat.format(date);

    }

    /**
     * 根据字符串时间获取对应时间戳
     *
     * @param date
     * @param format
     * @return
     */
    public static Integer getSecondFromDateString(String date, String format) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            Date d = simpleDateFormat.parse(date);
            return new Long(d.getTime() / 1000).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 前台DatetimeBox的字符串值转为时间戳
     *
     * @param dateStr 字符串日期
     * @return 秒级时间戳
     */
    public static Integer getSecondFromDateStringForDatetimeBox(String dateStr) {
        return DateUtils.getSecondFromDateString(dateStr, "MM/dd/yyyy HH:mm");
    }

    public static long diffDate(long timestamp) {
//        String a="2013-9-15 12:21:21";
        Date date = new Date();
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String b = sf.format(date);
//        Long c = sf.parse(b).getTime()-sf.parse(a).getTime();
//        Long c = sf.parse(b).getTime()- timestamp;
        long c = date.getTime() - timestamp * 1000;
        long d = c / 1000 / 60 / 60 / 24;//天
        return d;
    }

    /**
     * 得到本周周一
     *
     * @return
     */
    public static String getMondayOfThisWeek(String format) {
        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0)
            dayofweek = 7;
        c.add(Calendar.DATE, -dayofweek + 1);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(c.getTime());
    }

    /**
     * 得到本周周一00:00
     *
     * @return
     */
    public static Long getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();

        int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0)
            dayofweek = 7;
        c.add(Calendar.DATE, -dayofweek + 1);

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DAY_OF_MONTH, 0);

        return c.getTime().getTime() / 1000;
    }

    /**
     * 获取本月第一天的秒数
     *
     * @return
     */
    public static Long getFirstDateOfThisMonth() {
        Calendar c = Calendar.getInstance();

        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DAY_OF_MONTH, 0);

        return c.getTime().getTime() / 1000;
    }

    /**
     * 获取本月最后一天的秒数
     *
     * @return
     */
    public static Long getLastDateOfThisMonth() {//获取当月的最后一天
        Calendar lastDate = Calendar.getInstance();
        lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);//设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);//加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);//减去一天，变为当月最后一天
        return lastDate.getTime().getTime() / 1000;
    }

    /**
     * 得到本周周日
     *
     * @return
     */
    public static String getSundayOfThisWeek(String format) {
        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0)
            dayofweek = 7;
        c.add(Calendar.DATE, -dayofweek + 7);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(c.getTime());
    }

    /**
     * 获得本周周日的秒数
     *
     * @return
     */
    public static Long getSundayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 7);
        return c.getTime().getTime() / 1000;
    }

    /**
     * 获取当天00:00秒数
     *
     * @return
     */
    public static Long getCurrDateOfZero() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        return calendar.getTime().getTime() / 1000;
    }

    /**
     * 得到本月最后一天
     *
     * @return
     */
    public static String getLastDateOfMonth(String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
//        Date dt = getDate();
//        if (dt == null)
//            return null;
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dt);
//        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
//        cal.set(Calendar.DAY_OF_MONTH, days);
//        String result = format.format(cal.getTime());
//        System.out.println("一个月最后一天" + result);
//        return result;

        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());
        return last;
    }

    /**
     * 得到本月第一天
     *
     * @return
     */
    public static String getFirstDateOfMonth(String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
//        Date dt = getDate();
//        if (dt == null)
//            return null;
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dt);
//        int days = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
//        cal.set(Calendar.DAY_OF_MONTH, days);
//        String result = format.format(cal.getTime());
//        System.out.println("一个月第一天" + result);
//        return result;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());
        return first;
    }


    /**
     * 获得GMT今日零点时间 秒数
     */
    public static long getTodayZeroSecondGMT() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-0"));

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 获得GMT+8今日零点时间 秒数
     */
    public static long getTodayZeroSecond() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 获得GMT+8今日23时59分59秒时间 秒数
     */
    public static long getTodayEndSecond() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 获得GMT今日23时59分59秒时间 秒数
     */
    public static long getTodayEndSecondGMT() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-0"));

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 获得GMT明日23时59分59秒时间 秒数
     */
    public static long getTomorrowEndSecondGMT() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-0"));

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 秒数表示的时间是否是GMT-0今天
     */
    public static boolean isTodayGMT(Long time) {
        if (time >= getTodayZeroSecondGMT() && time <= getTodayEndSecondGMT()) {
            return true;
        }
        return false;
    }

    /**
     * 秒数表示的时间是否是GMT-0明天
     */
    public static boolean isTomorrowGMT(Long time) {
        if (time > getTodayEndSecondGMT() && time <= getTomorrowEndSecondGMT()) {
            return true;
        }
        return false;
    }

    /**
     * 秒数表示的时间是否是GMT-0今天或明天
     */
    public static boolean isTodayOrTomorrowGMT(Long time) {
        if (time >= getTodayZeroSecondGMT() && time <= getTomorrowEndSecondGMT()) {
            return true;
        }
        return false;
    }

    /**
     * 秒数是否落在闪购时间块
     */
    public static boolean isFlashTimeGMT(Long time) {
        if (time >= get24HoursAgoSecond() && time <= getTomorrowEndSecondGMT()) {
            return true;
        }
        return false;
    }

    /**
     * 秒数表示的时间对应的小时数
     */
    public static Integer getHour(Long seconds) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTimeInMillis(seconds * 1000);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 秒数表示的时间对应的GMT小时数
     */
    public static Integer getGMTHour(Long seconds) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-0"));
        calendar.setTimeInMillis(seconds * 1000);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获得时间对应的GMT小时数
     */
    public static Integer getGMTHour(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-0"));
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获得时间对应的GMT天数
     */
    public static Integer getGMTDay(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-0"));
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得24小时以前的秒数
     */
    public static Long get24HoursAgoSecond() {
        return DateUtils.getCurrentSecond() - 24 * 3600;
    }

    /**
     * 24小时的秒数
     *
     * @return
     */
    public static Integer get24HourSecond() {
        return 24 * 60 * 60;
    }

    /**
     * 今天之前的30天
     *
     * @return
     */
    public static Long getLastMonthTodaySecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(calendar.DAY_OF_MONTH, -30);
        return calendar.getTime().getTime() / 1000;
    }

    /**
     * 传入时间后30天的秒数
     *
     * @return
     */
    public static Long getNextThirtyTodaySecond(String dateStr) {
        Long nextThirtySecond = getSecondFromDateString(dateStr, "MM/dd/yyyy").longValue();
        nextThirtySecond += 30 * 24 * 3600;
        return nextThirtySecond;
    }

    /**
     * 传入时间前30天的秒数
     *
     * @return
     */
    public static Long getPriThirtyTodaySecond(String dateStr) {
        Long nextThirtySecond = getSecondFromDateString(dateStr, "MM/dd/yyyy").longValue();
        nextThirtySecond -= 30 * 24 * 3600;
        return nextThirtySecond;
    }

    /**
     * 30天的秒数
     *
     * @return
     */
    public static Long getThirtyDaySecond() {
        return 30 * 24 * 3600L;
    }

    public static Long getOneDaySecond() {
        return 24 * 3600L;
    }

    /**
     * 根据字符串日期获取相应的时间戳并改变偏移量
     *
     * @param date   字符串日期
     * @param format 格式化字符串
     * @param offset 偏移量 加一天或者减一天   传入-1  或者1
     * @return
     */
    public static Long getOffsetSecondFromDateFmtStr(String date, String format, int offset) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date dt = sdf.parse(date);
            calendar.setTime(dt);
            if (offset != 0) {
                calendar.add(calendar.DATE, offset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar.getTime().getTime() / 1000;
    }

    /**
     * 根据字符串日期获取相应的时间戳并且左偏移（减一天）
     *
     * @param date   字符串日期
     * @param format 格式化字符串
     * @return
     */
    public static Long getLeftOffsetSecondFromDateFmtStr(String date, String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date dt = sdf.parse(date);
            calendar.setTime(dt);
            calendar.add(calendar.DATE, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar.getTime().getTime() / 1000;
    }

    /**
     * 根据字符串日期获取相应的时间戳
     *
     * @param date   字符串日期
     * @param format 格式化字符串
     * @return
     */
    public static Long getSecondFromDateFmtStr(String date, String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date dt = sdf.parse(date);
            calendar.setTime(dt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar.getTime().getTime() / 1000;
    }

    /**
     * 根据字符串日期获取相应的时间戳并且右偏移（加一天）
     *
     * @param date   字符串日期
     * @param format 格式化字符串
     * @return
     */
    public static Long getRightOffsetSecondFromDateFmtStr(String date, String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date dt = sdf.parse(date);
            calendar.setTime(dt);
            calendar.add(calendar.DATE, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar.getTime().getTime() / 1000;
    }

    /**
     * 根据字符串日期获取相应的时间戳并且右偏移（加一天）
     *
     * @param date       字符串日期
     * @param offsetType 偏移类型，请参考Calender下的常量
     * @param offset     偏移量
     * @return 偏移后的日期
     */
    public static long getOffsetSecondFromValue(long date, int offsetType, int offset) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date dt = new Date(date * 1000L);
            calendar.setTime(dt);
            calendar.add(offsetType, offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar.getTime().getTime() / 1000;
    }

    /**
     * 秒数转日期
     *
     * @param time
     * @param formart
     * @return
     */
    public static String secondsToDate(Integer time, String formart) {
        if (time == null) {
            return null;
        }
        if (StringUtils.isBlank(formart)) {
            formart = "yyyy-MM-dd";
        }

        Date dat = new Date(time * 1000L);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        DateFormat df = new SimpleDateFormat(formart);
        return df.format(gc.getTime());
    }

    /**
     * 获得当天0点时间
     *
     * @return
     */
    public static Long getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * 获得当天24点时间
     *
     * @return
     */
    public static Long getTimesnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * 获得本周一0点时间
     *
     * @return
     */
    public static Long getTimesWeekmorning() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return currentDate.getTimeInMillis() / 1000;
    }

    /**
     * 获得本周日24点时间
     *
     * @return
     */
    public static Long getTimesWeeknight() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return currentDate.getTimeInMillis() / 1000;
    }

    /**
     * 获得本月第一天0点时间
     *
     * @return
     */
    public static Long getTimesMonthmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis() / 1000;
    }

    /**
     * 获得本月最后一天24点时间
     *
     * @return
     */
    public static Long getTimesMonthnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTimeInMillis() / 1000;
    }

    public static String getLastCharOfYear() {
        String date = getFormatDateFromSecond(System.currentTimeMillis() / 1000, "YYYY");
        return date.substring(3, 4);
    }

    /**
     * 获取N天的秒数
     *
     * @param day 天数
     */
    public static Integer getSecondByDay(int day) {
        return day * DAYTIME;
    }

    /**
     * 获取小时点秒数
     *
     * @param zeroSecond 0点秒数
     * @param hour       hour点秒数
     * @return
     */
    public static Integer getHourSecondByDay(int zeroSecond, int hour) {
        return zeroSecond + 3600 * hour;
    }

    /**
     * 校验是否是对应格式的数据
     *
     * @param dateStr
     * @param format
     * @return true符合格式
     */
    public static boolean isValidDate(String dateStr, String format) {

        DateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(dateStr);
            return dateStr.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 计算时间差
     *
     * @param lBefor 首日
     * @param dAfter 尾日
     * @return 时间差(毫秒)
     */
    public static final long getDateBetween(long lBefor, Date dAfter) {
        long lAfter = 0;
        long lRtn = 0;

        /** 取得距离 1970年1月1日 00:00:00 GMT 的毫秒数 */
        lAfter = dAfter.getTime();

        lRtn = lAfter - lBefor;

        return lRtn;
    }

    /**
     * 当前时间与参数时间差
     * >0 当前时间在传入时间之后，<0 当前时间在传入时间之前
     *
     * @param lBefor 首日
     * @return 时间差(分)
     */
    public static final int getDateBetweenNow(long lBefor) {

        return (int) (getDateBetween(lBefor, new Date()) / 1000 / 60);
    }

    /**
     * 获取string类型指定日期 ，小时，分钟，秒  的int类型时间。
     * 例如 date= 2016-09-10 10:10:20  获取这一天的0时0分0秒的时间.
     * getTimeScondInt(date,"yyyy-MM-dd HH:mm:ss",0,0,0);
     * getTimeScondInt(date,"yyyy-MM-dd HH:mm:ss",23,59,59);
     *
     * @param date
     * @param format
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static int getTimeScondInt(String date, String format, int hour, int minute, int second) {
        try {
            Date cdate = org.apache.commons.lang.time.DateUtils.parseDate(date, new String[]{format});
            Calendar ca = Calendar.getInstance();
            ca.setTime(cdate);
            ca.set(Calendar.HOUR_OF_DAY, hour);
            ca.set(Calendar.MINUTE, minute);
            ca.set(Calendar.SECOND, second);
            return (int) (ca.getTime().getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 更换日期格式
     *
     * @param dateStr   日期
     * @param oldFormat 日期格式
     * @param newFormat 新的日期格式
     * @return
     */
    public static String getFormatChange(String dateStr, String oldFormat, String newFormat) {
        SimpleDateFormat of = new SimpleDateFormat(oldFormat);
        Date date = null;
        try {
            date = of.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat nf = new SimpleDateFormat(newFormat);
        String newStr = nf.format(date.getTime());
        return newStr;
    }

    /**
     * 指定日期向前移动7天对应的日期
     * @param date
     * @param format
     * @return
     */
    public static Long getDateOfLaseServenDay(String date, String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date dt = sdf.parse(date);
            calendar.setTime(dt);
            calendar.add(calendar.DATE, -7);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar.getTime().getTime() / 1000;
    }

    /**
     * 获取指定日期一个月前的日期
     * @param date 指定日期
     * @param format 指定格式
     * @return
     */
    public static Long getDateOfLastMonth(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date newDate = sdf.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(newDate);
            return getDateOfLastMonth(c).getTime().getTime() / 1000;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format(yyyyMMdd): " + date);
        }
    }

    /**
     * 获取指定时间一个月前的时间
     * @param date
     * @return
     */
    private static Calendar getDateOfLastMonth(Calendar date) {
        Calendar lastDate = (Calendar) date.clone();
        lastDate.add(Calendar.MONTH, -1);
        return lastDate;
    }

    public static int getBetweenDays(String t1, String t2) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int betweenDays = 0;
        Date d1 = format.parse(t1);
        Date d2 = format.parse(t2);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        // 保证第二个时间一定大于第一个时间
        if(c1.after(c2)){
            c1 = c2;
            c2.setTime(d1);
        }
        int betweenYears = c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
        betweenDays = c2.get(Calendar.DAY_OF_YEAR)-c1.get(Calendar.DAY_OF_YEAR);
        for(int i=0;i<betweenYears;i++){
            int tmp=countDays(c1.get(Calendar.YEAR));
            betweenDays+=countDays(c1.get(Calendar.YEAR));
            c1.set(Calendar.YEAR,(c1.get(Calendar.YEAR)+1));
        }
        return betweenDays;
    }
    public static int countDays(int year){
        int n=0;
        for (int i = 1; i <= 12; i++) {
            n += countDays(i,year);
        }
        return n;
    }

    public static int countDays(int month, int year){
        int count = -1;
        switch(month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                count = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                count = 30;
                break;
            case 2:
                if(year % 4 == 0)
                    count = 29;
                else
                    count = 28;
                if((year % 100 ==0) & (year % 400 != 0))
                    count = 28;
        }
        return count;
    }


    /**
     * 转换为时间（天,时:分:秒.毫秒）
     * @param timeMillis
     * @return
     */
    public static String formatDateTime(long timeMillis){
        long day = timeMillis/(24*60*60*1000);
        long hour = (timeMillis/(60*60*1000)-day*24);
        long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
        long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
        long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
        return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }

}
