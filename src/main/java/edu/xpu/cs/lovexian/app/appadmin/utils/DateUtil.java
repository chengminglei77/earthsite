package edu.xpu.cs.lovexian.app.appadmin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author czy
 * @create 2021-05-03-19:50
 */
public class DateUtil {
    private static final String FULL_TIME_SPLIT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static Date toDate(Long time)
    {
        SimpleDateFormat fm1 = new SimpleDateFormat(FULL_TIME_SPLIT_PATTERN);
        String format = fm1.format(time);
        try {
            Date date = fm1.parse(format);
            return  date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String DateToString(Date date)
    {
        SimpleDateFormat fm1 = new SimpleDateFormat(FULL_TIME_SPLIT_PATTERN);
        String format = fm1.format(date.getTime());
        return format;
    }

    public static void main(String[] args) {
        System.out.println(DateToString(new Date()));
    }
}
