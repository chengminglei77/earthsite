package edu.xpu.cs.lovexian.common.utils;

import org.jsoup.helper.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**                                                                                ____________________
      _                _                                                           < 神兽护体，永无bug! >
    | |__  _   _  ___| |__   ___ _ __   __ _ _ __   ___ _ __   __ _                --------------------
   | '_ \| | | |/ __| '_ \ / _ \ '_ \ / _` | '_ \ / _ \ '_ \ / _` |                       \   ^__^
  | | | | |_| | (__| | | |  __/ | | | (_| | |_) |  __/ | | | (_| |                        \  (oo)\_______
 |_| |_|\__,_|\___|_| |_|\___|_| |_|\__, | .__/ \___|_| |_|\__, |                           (__)\       )\/\
                                   |___/|_|                |___/                                ||----w |
                                                                                                ||     ||
 * @author hcp
 * @version 1.0.0
 * @create 11:27 2019/11/1
 * 时间工具类
 */
public class DateUtil {

    public static final String FULL_TIME_PATTERN = "yyyyMMddHHmmss";
    public static final String YYYYMMDD = "yyyyMMdd";

    public static final String FULL_TIME_SPLIT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String YMD = "yyyy-MM-dd";

    public static String formatFullTime(LocalDateTime localDateTime) {
        return formatFullTime(localDateTime, FULL_TIME_PATTERN);
    }

    public static String formatFullTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    public static String getDateFormat(Date date, String dateFormatType) {
        SimpleDateFormat simformat = new SimpleDateFormat(dateFormatType);
        return simformat.format(date);
    }

    public static String formatCSTTime(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date d = sdf.parse(date);
        return DateUtil.getDateFormat(d, format);
    }

    public static String formatInstant(Instant instant, String format) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate;
    }

    /**
     * 日期向后或者向前推移
     * @param dateStr 日期字符串，如："2019-11-11"
     * @param intervalDay 日期推移值；如：1，传入日期参数向后推一天；-1，传入日期参数向前推一天
     * @return 返回推移后的日期字符串
     */
    public static String dayAddOrCut(String dateStr, int intervalDay){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try{
            date = sdf.parse(dateStr);
        }catch(ParseException e){
            e.printStackTrace();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, intervalDay);
        date = calendar.getTime();
        return dateStr = sdf.format(date);
    }

    /**
     * 字符串转换成日期
     * @param str 格式为yyyy-MM-dd HH:mm:ss
     * @return date
     */
    public static Date StrToDate(String str,String format){
        if(StringUtil.isBlank(format)){
            format="yyyy-MM-dd HH:mm:ss";
        }
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate 较大的时间
     * @param containEndDate 是否包含结束日期，如果包含，则多加1
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate,Date bdate,boolean containEndDate) throws ParseException{
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            return daysBetween(sdf.format(smdate),sdf.format(bdate),containEndDate);
        }catch(Exception e){
        }
        return 0;
    }

    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate,String bdate,boolean containEndDate){
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            return days(sdf.parse(smdate),sdf.parse(bdate),containEndDate);
        }catch(Exception e){
        }
        return 0;
    }

    private static int days(Date smdate,Date bdate,boolean containEndDate) throws Exception{
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24)+(containEndDate?1:0);
        return Integer.parseInt(String.valueOf(between_days));
    }
}
