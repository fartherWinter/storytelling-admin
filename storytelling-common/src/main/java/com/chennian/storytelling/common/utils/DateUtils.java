package com.chennian.storytelling.common.utils;

//import javafx.stage.StageStyle;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class DateUtils {
    public static final long m_second = 1000;
    public static final long m_minute = m_second * 60;
    public static final long m_hour = m_minute * 60;
    public static final long m_day = m_hour * 24;
    public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String PATTERN_YYYYMMDD = "yyyyMMdd";
    public static final String PATTERN_YYYYMM = "yyyyMM";
    public static final String PATTERN_HH_MM_SS = "HH:mm:ss";
    public static final String MAX_TIMESTAMP = "2038-01-01 00:00:00";

    /**
     * <p>DateUtil instances should NOT be constructed in standard programming.</p>
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public DateUtils() {
    }



    /**
     * 判断当前时间是否在一个固定时间区间内
     */
    public static boolean workHours() {
        LocalTime startTime = LocalTime.of(9, 0, 0);
        LocalTime endTime = LocalTime.of(18, 0, 0);
        LocalTime currentTime = LocalTime.now();
        if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }


    public static String getYyyyMMdd() {
        Date date = new Date();
        return format(date, PATTERN_YYYYMMDD);
    }

    /**
     * 计算两个时间相差分钟数
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public static long findMin(Date beginTime, Date endTime) {
        SimpleDateFormat dfs = new SimpleDateFormat(PATTERN_YYYY_MM_DD_HH_MM_SS);
        try {
            long between = (endTime.getTime() - beginTime.getTime()) / 1000;
            long min = between / 60;
            return min;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取系统时间戳，毫秒级
     *
     * @return
     */
    public static final long timeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 当前日期字符串，yyyy-MM-dd
     *
     * @return
     */
    public static final String currentDateStr() {
        return formatDate(currentTime());
    }

    /**
     * 获取当前日期
     * <br>参见{@link #timeMillis()}
     *
     * @return
     */
    public static final Date currentTime() {
        return new Date();
    }

    /**
     * 当前timestamp字符串，yyyy-MM-dd HH:mm:ss
     * <br>参见{@link #format(Date, String)}
     *
     * @return
     */
    public static final String getCurFullTimestampStr() {
        return formatTimestamp(getCurFullTimestamp());
    }

    /**
     * 当前timestamp
     * <br>字符串类型返回，参见{@link  #currentTimestampStr()}
     *
     * @return
     */
    public static final Timestamp getCurFullTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 当前月份的下一个月
     * <br>1月份的下一个月为 2，12月份的下一个月为1
     *
     * @return
     */
    public static final int nextMonth() {
        String next = format(new Date(), "M");
        int nextMonth = Integer.parseInt(next) + 1;
        if (nextMonth == 13) return 1;
        return nextMonth;
    }

    /**
     * parse date using default pattern yyyy-MM-dd
     *
     * @param strDate
     * @return 失败返回null
     */
    public static final Date parseDate(String strDate) {
        return parseDate(strDate, PATTERN_YYYY_MM_DD);
    }

    public static final Date parseDateTime(String strDate) {
        return parseDate(strDate, PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    public static int getWeekOfYear(Timestamp time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.getDateFromTimestamp(time));
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        return week;
    }

    /**
     * 中国传统意义的周，周一做为开始
     *
     * @param time
     * @return
     */
    public static int getCnWeekOfYear(Timestamp time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.getDateFromTimestamp(time));
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        return week;
    }

    /**
     * 根据date字符串，获取timestamp
     *
     * @param strDate yyyy-MM-dd hh:mm:ss
     * @return 失败返回null
     */
    public static final Timestamp parseTimestamp(String strDate) {
        return parseTimestamp(strDate, PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * @param strDate
     * @param pattern
     * @return
     */
    public static final Timestamp parseTimestamp(String strDate, String pattern) {
        Date date = parseDate(strDate, pattern);
        if (date == null) return null;
        return new Timestamp(date.getTime());
    }

    /**
     * @param strDate
     * @param pattern
     * @return
     */
    public static final Date parseDate(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (Exception pe) {
            return null;
        }
    }

    /**
     * @param date
     * @return formated date by yyyy-MM-dd
     */
    public static final <T extends Date> String formatDate(T date) {
        if (date == null) return null;
        return format(date, PATTERN_YYYY_MM_DD);
    }

    /**
     * @param date
     * @return formated time by HH:mm:ss
     */
    public static final <T extends Date> String formatTime(T date) {
        return format(date, PATTERN_HH_MM_SS);
    }

    /**
     * @param date
     * @return formated time by yyyy-MM-dd HH:mm:ss
     */
    public static final <T extends Date> String formatTimestamp(T date) {
        return format(date, PATTERN_YYYY_MM_DD_HH_MM_SS);
    }


    public static final String formatTimestamp(Long mills) {
        return formatTimestamp(new Date(mills));
    }

    /**
     * @param date
     * @param pattern: Date format pattern
     * @return
     */
    public static final <T extends Date> String format(T date, String pattern) {
        if (date == null) return null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param original
     * @param days
     * @param hours
     * @param minutes
     * @param seconds
     * @param mill
     * @return original+day+hour+minutes+seconds+millseconds
     */
    public static final <T extends Date> T addTime(T original, int days, int hours, int minutes, int seconds) {
        if (original == null) return null;
        long newTime = original.getTime() + m_day * days + m_hour * hours + m_minute * minutes + m_second * seconds;
        T another = (T) original.clone();
        another.setTime(newTime);
        return another;
    }

    public static final <T extends Date> T addDay(T original, int days) {
        if (original == null) return null;
        long newTime = original.getTime() + m_day * days;
        T another = (T) original.clone();
        another.setTime(newTime);
        return another;
    }

    public static final <T extends Date> T addMonth(T original, int months) {
        if (original == null)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(original);
        calendar.add(Calendar.MONTH, months);
        T another = (T) original.clone();
        another.setTime(calendar.getTimeInMillis());
        return another;
    }

    public static final <T extends Date> T addHour(T original, int hours) {
        if (original == null) return null;
        long newTime = original.getTime() + m_hour * hours;
        T another = (T) original.clone();
        another.setTime(newTime);
        return another;
    }

    public static final <T extends Date> T addMinute(T original, int minutes) {
        if (original == null) return null;
        long newTime = original.getTime() + m_minute * minutes;
        T another = (T) original.clone();
        another.setTime(newTime);
        return another;
    }

    public static final <T extends Date> T addSecond(T original, int second) {
        if (original == null) return null;
        long newTime = original.getTime() + m_second * second;
        T another = (T) original.clone();
        another.setTime(newTime);
        return another;
    }

    /**
     * @param day
     * @return for example ,1997/01/02 22:03:00,return 1997/01/02 00:00:00.0
     */
    public static final <T extends Date> T getBeginningTimeOfDay(T day) {
        if (day == null) return null;
        //new Date(0)=Thu Jan 01 08:00:00 CST 1970
        String strDate = formatDate(day);
        Long mill = parseDate(strDate).getTime();
        T another = (T) day.clone();
        another.setTime(mill);
        return another;
    }

    /**
     * @param day
     * @return for example ,1997/01/02 22:03:00,return 1997/01/02 23:59:59.999
     */
    public static final <T extends Date> T getLastTimeOfDay(T day) {
        if (day == null) return null;
        Long mill = getBeginningTimeOfDay(day).getTime() + m_day - 1;
        T another = (T) day.clone();
        another.setTime(mill);
        return another;
    }

    /**
     * 09:00:00,09:07:00 ---> 9:00,9:7:00
     *
     * @param time
     * @return
     */
    public static final String formatTime(String time) {
        if (time == null) return null;
        time = StringUtils.trim(time);
        if (StringUtils.isBlank(time)) throw new IllegalArgumentException("时间格式有错误！");
        time = time.replace('：', ':');
        String[] times = time.split(":");
        String result = "";
        if (times[0].length() < 2) result += "0" + times[0] + ":";
        else result += times[0] + ":";
        if (times.length > 1) {
            if (times[1].length() < 2) result += "0" + times[1];
            else result += times[1];
        } else {
            result += "00";
        }
        Timestamp.valueOf("2001-01-01 " + result + ":00");
        return result;
    }

    public static boolean isTomorrow(Date date) {
        if (date == null) return false;
        if (formatDate(addTime(new Date(), 1, 0, 0, 0)).equals(formatDate(date))) return true;
        return false;
    }

    /***
     * @param date
     * @return 1, 2, 3, 4, 5, 6, 7
     */
    private static int[] chweek = new int[]{0, 7, 1, 2, 3, 4, 5, 6};

    /**
     * @param date
     * @return 1, 2, 3, 4, 5, 6, 7
     */
    public static Integer getWeek(Date date) {
        if (date == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return chweek[c.get(Calendar.DAY_OF_WEEK)];
    }

    public static Date getCurDateByWeek(Integer week) {
        if (week == null || week < 0 || week > 7) return DateUtils.currentTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, week);
        return calendar.getTime();
    }

    public static String[] cnweek = new String[]{"", "周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    public static String[] cnweek2 = new String[]{"", "周7", "周1", "周2", "周3", "周4", "周5", "周6"};
    private static String[] cnSimpleweek = new String[]{"", "日", "一", "二", "三", "四", "五", "六"};

    /**
     * @param date
     * @return "周日", "周一", "周二", "周三", "周四", "周五", "周六"
     */
    public static String getCnWeek(Date date) {
        if (date == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return cnweek[c.get(Calendar.DAY_OF_WEEK)];
    }

    public static String getCnWeek2(Date date) {
        if (date == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return cnweek[c.get(Calendar.DAY_OF_WEEK)];
    }

    /**
     * @param date
     * @return "日", "一", "二", "三", "四", "五", "六"
     */
    public static String getCnSimpleWeek(Date date) {
        if (date == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return cnSimpleweek[c.get(Calendar.DAY_OF_WEEK)];
    }

    public static Integer getCurrentDay() {
        return getDay(new Date());
    }

    public static Integer getCurrentMonth() {
        return getMonth(new Date());
    }

    public static Integer getCurrentYear() {
        return getYear(new Date());
    }

    public static Integer getYear(Date date) {
        if (date == null) return null;
        String year = DateUtils.format(date, "yyyy");
        return Integer.parseInt(year);
    }

    public static Integer getDay(Date date) {
        if (date == null) return null;
        String year = DateUtils.format(date, "d");
        return Integer.parseInt(year);
    }

    public static Timestamp getYearFirstDay(Date date) {
        Integer year = getYear(date);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, 0, 1, 0, 0, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Timestamp getYearLastDay(Date date) {
        Integer year = getYear(date);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, 11, 31, 23, 59, 59);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * @param date
     * @return 日期所在月份
     */
    public static Integer getMonth(Date date) {
        if (date == null) return null;
        String month = format(date, "M");
        return Integer.parseInt(month);
    }

    public static Integer getCurrentHour(Date date) {
        if (date == null) return null;
        String hour = DateUtils.format(date, "H");
        return Integer.parseInt(hour);
    }

    public static Integer getCurrentMin(Date date) {
        if (date == null) return null;
        String hour = DateUtils.format(date, "m");
        return Integer.parseInt(hour);
    }

    public static String getCurDateStr() {
        return DateUtils.formatDate(new Date());
    }

    public static String getCurTimeStr() {
        return DateUtils.formatTimestamp(new Date());
    }

    public static boolean isAfter(Date date) {
        if (date == null) return false;
        if (date.after(new Date())) {
            return true;
        }
        return false;
    }

    /**
     * 获取date所在月份的星期为weektype且日期在date之后（或等于）的所有日期
     *
     * @param weektype
     * @return
     */
    public static List<Date> getWeekDateList(Date date, String weektype) {
        int curMonth = getMonth(date);
        int week = Integer.parseInt(weektype);
        int curWeek = getWeek(date);
        int sub = (7 + week - curWeek) % 7;
        Date next = addDay(date, sub);
        List<Date> result = new ArrayList<Date>();
        while (getMonth(next) == curMonth) {
            result.add(next);
            next = addDay(next, 7);
        }
        return result;
    }

    /**
     * 获取date之后(包括date)的num个星期为weektype日期（不限制月份）
     *
     * @param weektype
     * @return
     */
    public static List<Date> getWeekDateList(Date date, String weektype, int num) {
        int week = Integer.parseInt(weektype);
        int curWeek = getWeek(date);
        List<Date> result = new ArrayList<Date>();
        int sub = (7 + week - curWeek) % 7;
        Date next = addDay(date, sub);
        for (int i = 0; i < num; i++) {
            result.add(next);
            next = addDay(next, 7);
        }
        return result;
    }

    /**
     * 获取date所在星期的周一至周日的日期
     *
     * @param date
     * @return
     */
    public static List<Date> getCurWeekDateList(Date date) {
        int curWeek = getWeek(date);
        List<Date> dateList = new ArrayList<Date>();
        for (int i = 1; i <= 7; i++) dateList.add(DateUtils.addDay(date, -curWeek + i));
        return dateList;
    }

    public static Date getWeekLastDay(Date date) {
        int curWeek = getWeek(date);
        return DateUtils.addDay(date, 7 - curWeek);
    }

    public static Date getCurDate() {
        return getBeginningTimeOfDay(new Date());
    }

    /**
     * 获取日期所在月份的第一天
     *
     * @param date
     * @return
     */
    public static <T extends Date> T getMonthFirstDay(T date) {
        if (date == null) return null;
        String dateStr = format(date, "yyyy-MM") + "-01";
        Long mill = parseDate(dateStr).getTime();
        T another = (T) date.clone();
        another.setTime(mill);
        return another;
    }

    public static <T extends Date> T getNextMonthFirstDay(T day) {
        if (day == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month + 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String datefor = format(calendar.getTime(), "yyyy-MM-dd");
        Long mill = parseDate(datefor).getTime();
        T another = (T) day.clone();
        another.setTime(mill);
        return another;
    }

    /**
     * 获取日期所在月份的最后一天
     *
     * @param days
     * @return
     */
    public static <T extends Date> T getMonthLastDay(T date) {
        if (date == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String dateStr = format(date, "yyyy-MM") + "-" + c.getActualMaximum(Calendar.DAY_OF_MONTH);
        Long mill = parseDate(dateStr).getTime();
        T another = (T) date.clone();
        another.setTime(mill);
        return another;
    }

    public static String formatDate(int days) {
        return formatDate(addDay(new Date(), days));
    }

    /**
     * 截取时分秒后的时间
     *
     * @return
     */
    public static Timestamp getCurTruncTimestamp() {
        return getBeginningTimeOfDay(new Timestamp(System.currentTimeMillis()));
    }

    public static Integer getHour(Date date) {
        if (date == null) return null;
        String hour = format(date, "H");
        return Integer.parseInt(hour);
    }

    public static Integer getMinute(Date date) {
        if (date == null) return null;
        String m = format(date, "m");
        return Integer.parseInt(m);
    }

    public static String getTimeDesc(Timestamp time) {
        if (time == null) return "";
        String timeContent;
        Long ss = System.currentTimeMillis() - time.getTime();
        Long minute = ss / 60000;
        if (minute < 1) {
            Long second = ss / 1000;
            timeContent = second + "秒前";
        } else if (minute >= 60) {
            Long hour = minute / 60;
            if (hour >= 24) {
                if (hour > 720) timeContent = "1月前";
                else if (hour > 168 && hour <= 720) timeContent = (hour / 168) + "周前";
                else timeContent = (hour / 24) + "天前";
            } else {
                timeContent = hour + "小时前";
            }
        } else {
            timeContent = minute + "分钟前";
        }
        return timeContent;
    }

    public static String getDateDesc(Date time) {
        if (time == null) return "";
        String timeContent;
        Long ss = System.currentTimeMillis() - time.getTime();
        Long minute = ss / 60000;
        if (minute < 1) {
            Long second = ss / 1000;
            timeContent = second + "秒前";
        } else if (minute >= 60) {
            Long hour = minute / 60;
            if (hour >= 24) {
                if (hour > 720) timeContent = "1月前";
                else if (hour > 168 && hour <= 720) timeContent = (hour / 168) + "周前";
                else timeContent = (hour / 24) + "天前";
            } else {
                timeContent = hour + "小时前";
            }
        } else {
            timeContent = minute + "分钟前";
        }
        return timeContent;
    }

    /**
     * author: bob
     * date:	20100729
     * 截取日期, 去掉年份
     * param: 	date1
     * eg. 传入"1986-07-28", 返回 07-28
     */
    public static String getMonthAndDay(Date date) {
        return formatDate(date).substring(5);
    }

    public static Date getMillDate() {
        return new Date();
    }

    public static Timestamp getMillTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 时间差：day1-day2
     *
     * @param day1
     * @param day2
     * @return
     */
    public static final <T extends Date> String getDiffDayStr(T day1, T day2) {
        if (day1 == null || day2 == null) return "---";
        long diff = day1.getTime() - day2.getTime();
        long sign = diff / Math.abs(diff);
        if (sign < 0) return "已经过期";
        diff = Math.abs(diff) / 1000;
        long day = diff / 3600 / 24;
        long hour = (diff - (day * 3600 * 24)) / 3600;
        long minu = diff % 3600 / 60;
        return (day == 0 ? "" : day + "天") + (hour == 0 ? "" : hour + "小时") + (minu == 0 ? "" : minu + "分");
    }

    /**
     * 时间差：day1-day2
     *
     * @param day1
     * @param day2
     * @return
     */
    public static final <T extends Date> String getDiffStr(T day1, T day2) {
        if (day1 == null || day2 == null) return "---";
        long diff = day1.getTime() - day2.getTime();
        long sign = diff / Math.abs(diff);
        diff = Math.abs(diff) / 1000;
        long hour = diff / 3600;
        long minu = diff % 3600 / 60;
        long second = diff % 60;
        return (sign < 0 ? "-" : "+") + (hour == 0 ? "" : hour + "小时") + (minu == 0 ? "" : minu + "分") + (second == 0 ? "" : second + "秒");
    }

    public static final <T extends Date> String getDiffStrX(T day1, T day2) {
        if (day1 == null || day2 == null) return "---";
        long diff = day1.getTime() - day2.getTime();
        long sign = diff / Math.abs(diff);
        diff = Math.abs(diff) / 1000;
        long hour = diff / 3600;
        long minu = diff % 3600 / 60;
        long second = diff % 60;
        return (hour == 0 ? "" : hour + "小时") + (minu == 0 ? "" : minu + "分") + (second == 0 ? "" : second + "秒");
    }

    /**
     * 时间差（秒）：day1-day2
     *
     * @param day1
     * @param day2
     * @return
     */
    public static final <T extends Date> long getDiffSecond(T day1, T day2) {
        if (day1 == null || day2 == null) return 0;
        long diff = day1.getTime() - day2.getTime();
        if (diff == 0) return 0;
        long sign = diff / Math.abs(diff);
        diff = Math.abs(diff) / 1000;
        return sign * diff;
    }

    /**
     * 时间差（分钟）：day1-day2
     *
     * @param day1
     * @param day2
     * @return
     */
    public static final <T extends Date> double getDiffMinu(T day1, T day2) {
        if (day1 == null || day2 == null) return 0;
        long diff = day1.getTime() - day2.getTime();
        if (diff == 0) return 0;
        long sign = diff / Math.abs(diff);
        diff = Math.abs(diff) / 1000;
        return Math.round(diff * 1.0d * 10 / 6.0) / 100.0 * sign;//两位小数
    }

    /**
     * 时间差（分）：time1 - time2
     *
     * @param time1
     * @param time2
     * @return
     */
    public static final double getMillDiffMinu(long time1, long time2) {
        long diff = time1 - time2;
        if (diff == 0) return 0;
        long sign = diff / Math.abs(diff);
        diff = Math.abs(diff) / 1000;
        return Math.round(diff * 1.0d * 10 / 6.0) / 100.0 * sign;//两位小数
    }

    /**
     * 时间差（小时）：day1 - day2
     *
     * @param day1
     * @param day2
     * @return
     */
    public static final <T extends Date> double getDiffHour(T day1, T day2) {
        if (day1 == null || day2 == null) return 0;
        long diff = day1.getTime() - day2.getTime();
        long sign = diff / Math.abs(diff);
        diff = Math.abs(diff) / 1000;
        return Math.round(diff * 1.0d / 3.6) / 1000.0 * sign;//三位小数
    }

    /**
     * @param day1
     * @param day2
     * @return 日期相差整数round(abs （ day1 - day2))
     */
    public static final <T extends Date> int getDiffDay(T day1, T day2) {
        if (day1 == null || day2 == null) return 0;
        long diff = day1.getTime() - day2.getTime();
        diff = Math.abs(diff) / 1000;
        return Math.round(diff / (3600 * 24));
    }

    public static final <T extends Date> int getDiffDay2(T day1, T day2) {
        if (day1 == null || day2 == null) return 0;
        long diff = day1.getTime() - day2.getTime();
        diff = diff / 1000;
        return Math.round(diff / (3600 * 24));
    }

    public static boolean isAfterOneHour(Date date, String time) {
        String datetime = formatDate(date) + " " + time + ":00";
        if (addHour(parseTimestamp(datetime), -1).after(getMillTimestamp())) {
            return true;
        }
        return false;
    }

    public static boolean isValidDate(String fyrq) {
        return DateUtils.parseDate(fyrq) != null;
    }

    public static <T extends Date> long getCurDateMills(T date) {
        if (date == null) return 0;
        return date.getTime();
    }

    /**
     * eg.  1997/01/02 22:03:00,return 1997/01/02 00:00:00.0
     **/
    public static Timestamp getBeginTimestamp(Date date) {
        return new Timestamp(getBeginningTimeOfDay(date).getTime());
    }

    public static Timestamp getEndTimestamp(Date date) {
        return new Timestamp(getLastTimeOfDay(date).getTime());
    }

    /**
     * @param timestamp
     * @return
     */
    public static Date getDateFromTimestamp(Timestamp timestamp) {
        if (timestamp == null) return null;
        return new Date(timestamp.getTime());
    }

    public static int after(Date date1, Date date2) {
        date1 = getBeginningTimeOfDay(date1);
        date2 = getBeginningTimeOfDay(date2);
        return date1.compareTo(date2);
    }

    public static Timestamp mill2Timestamp(Long mill) {
        if (mill == null) return null;
        return new Timestamp(mill);
    }

    public static int subCurTimeSend() {
        Timestamp curtime = DateUtils.getCurFullTimestamp();
        Timestamp endtime = DateUtils.getLastTimeOfDay(curtime);
        Long scopeSecond = DateUtils.getDiffSecond(endtime, curtime);
        return scopeSecond.intValue();
    }

    /**
     * @param date
     * @param pattern: Date format pattern
     * @return
     */
    public static final <T extends Date> String formatEn(T date, String pattern) {
        if (date == null) return null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.ENGLISH);
            String result = df.format(date);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCurrAddHour(int hour) {
        return formatTimestamp(System.currentTimeMillis() + m_hour * hour);
    }

    public static String getCurrAddDay(int day) {
        return formatTimestamp(System.currentTimeMillis() + m_day * day);
    }

    public static final Timestamp parseDate2Timestamp(Date date) {
        return parseTimestamp(formatTimestamp(date));
    }

    public static final String currentTimeFull() {
        return format(new Date(), "yyyyMMddHHmmss");
    }

    public static String getMinutetime(long time) {
        if (time <= 0) return "";

        int ss = 1;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = time / dd;
        long hour = (time - day * dd) / hh;
        long minute = (time - day * dd - hour * hh) / mi;
        long second = (time - day * dd - hour * hh - minute * mi) / ss;

        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;

        if (StringUtils.equals(strHour, "00")) {
            String minutetime = strMinute + "分" + strSecond + "秒";
            return minutetime;
        }
        String minutetime = strHour + "小时" + strMinute + "分" + strSecond + "秒";
        return minutetime;
    }

    public static final <T extends Date> String getDiffStrHM(T day1, T day2) {
        if (day1 == null || day2 == null) return "---";
        long diff = day1.getTime() - day2.getTime();
        diff = Math.abs(diff) / 1000;
        long hour = diff / 3600;
        long minu = diff % 3600 / 60;
        return (hour == 0 ? "" : hour + "小时") + (minu == 0 ? "" : minu + "分");
    }

    public static final <T extends Date> long getDiffSecond2(T day1, T day2) {
        if (day1 == null || day2 == null) return 0;
        long diff = day1.getTime() - day2.getTime();
        if (diff <= 0) return 0;
        long sign = diff / Math.abs(diff);
        diff = Math.abs(diff) / 1000;
        return sign * diff;
    }

    /**
     * 过去的时间如上，
     * 未来时间：
     * 明天，后天，
     *
     * @param time
     * @return
     */
    public static String getTimeDesc3(Timestamp time) {
        if (time == null) return "";
        //String timeContent;
        Long paramsTimes = getBeginningTimeOfDay(time).getTime();
        if (getCurDate().getTime() == paramsTimes) return "今天" + formatTimeH(time);
        if (addDay(getCurDate(), 1).getTime() == paramsTimes) return "明天" + formatTimeH(time);
        if (addDay(getCurDate(), 2).getTime() == paramsTimes) return "后天" + formatTimeH(time);
        else {
            return formatTimeM(time);
        }
    }

    public static String getTimeDesc4(Timestamp time) {
        if (time == null) return "";
        Long paramsTimes = getBeginningTimeOfDay(time).getTime();
        if (getCurDate().getTime() == paramsTimes) {
            return "今天";
        }
        if (addDay(getCurDate(), 1).getTime() == paramsTimes) {
            return "明天";
        }
        if (addDay(getCurDate(), 2).getTime() == paramsTimes) {
            return "后天";
        }
        return getCnWeek(time);
    }

    public static final <T extends Date> String formatTimeH(T date) {
        if (date == null) return null;
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(date);
    }

    public static final <T extends Date> String formatTimeM(T date) {
        if (date == null) return null;
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return timeFormat.format(date);
    }

    public static String getTimeDesc2(Date time) {
        if (time == null) return "";
        String timeContent;
        Long ss = System.currentTimeMillis() - time.getTime();
        Long minute = ss / 60000;

        if (minute < 1) return "刚刚";
        if (minute >= 60) {
            Long hour = minute / 60;
            if (hour >= 24) {
                Integer curYear = getCurrentYear();
                Integer nowYear = DateUtils.getYear(time);
                if (curYear > nowYear) timeContent = format(time, "yyyy-MM-dd");
                else timeContent = format(time, "MM-dd");
            } else {
                timeContent = hour + "小时前";
            }
        } else {
            timeContent = minute + "分钟前";
        }
        return timeContent;
    }

    public static final <T extends Date> int getDiffYear(T day1, T day2) {
        if (day1 == null || day2 == null)
            return 0;
        long diff = day1.getTime() - day2.getTime();
        diff = Math.abs(diff) / 1000;
        return Math.round(diff / (3600 * 24 * 365));
    }

    public static Date getWeekFirstDay(Date date) {
        if (date == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, 1);
        return c.getTime();
    }

    /**
     * 判断当前时间是否在某个时间区间之内
     *
     * @param startTime
     * @param endTime
     */
    public static Boolean betweenTime(Date startTime, Date endTime) {
        Date currentTime = DateUtils.currentTime();
        if (currentTime.after(startTime) && currentTime.before(endTime)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * 获取当前时间到零点的时间差
     *
     * @return
     */
    public static long getTimeDifference() {
        // 当前日期、时间
        LocalDate currentDate = LocalDate.now();
        LocalDateTime currentDateTime = LocalDateTime.now();
        // 零点
        LocalTime expireTime = LocalTime.of(0, 0, 0);
        LocalDateTime expireDateTime = LocalDateTime.of(currentDate.plusDays(1), expireTime);
        // 计算时间差
        Duration duration = Duration.between(currentDateTime, expireDateTime);
        // 返回时间差（秒）
        return duration.getSeconds();
    }

}
