package edu.xpu.cs.lovexian.common.utils;

import java.math.BigInteger;


public class WindSpeedUtils {
    /**
     * 解码风速传感器的工具类
     * @param message
     * @return
     */
    /**********************************1号机*****************************/
    //3个时间段的风速

    /**
     * 3s平均风速
     */
    public static double windSpeed_3s_no1(String message){
        //1号传感器3s时的平均风速和风向
        String sensor_data_speed_3s_no1 = message.substring(22,26);
        String speed_3s_no1 = new BigInteger(sensor_data_speed_3s_no1,16).toString(10);
        double windSpeed_3s_no1 = Integer.parseInt(speed_3s_no1)/100.0;
        return windSpeed_3s_no1;
    }

    /**
     * 2min平均风速
     */
    public static double windSpeed_2min_no1(String message){
        //1号传感器2min时的平均风速和风向
        String sensor_data_speed_2min_no1 = message.substring(30,34);
        String speed_2min_no1 = new BigInteger(sensor_data_speed_2min_no1,16).toString(10);
        double windSpeed_2min_no1 = Integer.parseInt(speed_2min_no1)/100.0;
        return windSpeed_2min_no1;

    }
    /**
     * 10min平均风速
     */
    public static double windSpeed_10min_no1(String message){
        String sensor_data_speed_10min_no1 = message.substring(38,42);
        String speed_10min_no1 = new BigInteger(sensor_data_speed_10min_no1,16).toString(10);
        double windSpeed_10min_no1 = Integer.parseInt(speed_10min_no1)/100.0;
        return windSpeed_10min_no1;
    }

    //3个时间段的风向

    /**
     *3s平均风向
     */
    public static double windDirection_3s_no1(String message){
        String sensor_data_direction_3s_no1 = message.substring(26,30);
        String direction_3s_no1 = new BigInteger(sensor_data_direction_3s_no1,16).toString(10);
        int windDirection_3s_no1 = Integer.parseInt(direction_3s_no1)+92;
        if (windDirection_3s_no1 > 359){
            windDirection_3s_no1 = windDirection_3s_no1-359;
            return windDirection_3s_no1;
        }
        return windDirection_3s_no1;
    }


    /**
     * 2min风向
     */
    public static double windDirection_2min_no1(String message){
        String sensor_data_direction_2min_no1 = message.substring(34,38);
        String direction_2min_no1 = new BigInteger(sensor_data_direction_2min_no1,16).toString(10);
        int windDirection_2min_no1 = Integer.parseInt(direction_2min_no1)+92;
        if (windDirection_2min_no1 > 359){
            windDirection_2min_no1 = windDirection_2min_no1-359;
            return windDirection_2min_no1;
        }
        return windDirection_2min_no1;
    }

    /**
     * 10min风向
     */
    public static double windDirection_10min_no1(String message){
        String sensor_data_direction_10min_no1 = message.substring(42,46);
        String direction_10min_no1 = new BigInteger(sensor_data_direction_10min_no1,16).toString(10);
        int windDirection_10min_no1 = Integer.parseInt(direction_10min_no1)+92;
        if (windDirection_10min_no1 > 359){
            windDirection_10min_no1 = windDirection_10min_no1-359;
            return windDirection_10min_no1;
        }
        return windDirection_10min_no1;
    }




    /**********************************2号机*****************************/
    /**
     * 3s平均风速
     */
    public static double windSpeed_3s_no2(String message){
        //1号传感器3s时的平均风速和风向
        String sensor_data_speed_3s_no2 = message.substring(46,50);
        String speed_3s_no2 = new BigInteger(sensor_data_speed_3s_no2,16).toString(10);
        double windSpeed_3s_no2 = Integer.parseInt(speed_3s_no2)/100.0;
        return windSpeed_3s_no2;
    }

    /**
     * 2min平均风速
     */
    public static double windSpeed_2min_no2(String message){
        //1号传感器2min时的平均风速和风向
        String sensor_data_speed_2min_no2 = message.substring(54,58);
        String speed_2min_no2 = new BigInteger(sensor_data_speed_2min_no2,16).toString(10);
        double windSpeed_2min_no2 = Integer.parseInt(speed_2min_no2)/100.0;
        return windSpeed_2min_no2;

    }
    /**
     * 10min平均风速
     */
    public static double windSpeed_10min_no2(String message){
        String sensor_data_speed_10min_no2 = message.substring(62,66);
        String speed_10min_no2 = new BigInteger(sensor_data_speed_10min_no2,16).toString(10);
        double windSpeed_10min_no2 = Integer.parseInt(speed_10min_no2)/100.0;
        return windSpeed_10min_no2;
    }

    //3个时间段的风向

    /**
     *3s平均风向
     */
    public static double windDirection_3s_no2(String message){
        String sensor_data_direction_2min_no2 = message.substring(50,54);
        String direction_3s_no2 = new BigInteger(sensor_data_direction_2min_no2,16).toString(10);
        int windDirection_3s_no2 = Integer.parseInt(direction_3s_no2)-22;
        if (windDirection_3s_no2 < 0){
            windDirection_3s_no2 = windDirection_3s_no2+359;
            return windDirection_3s_no2;
        }
        return windDirection_3s_no2;
    }


    /**
     * 2min风向
     */
    public static double windDirection_2min_no2(String message){
        String sensor_data_direction_2min_no2 = message.substring(58,62);
        String direction_2min_no2 = new BigInteger(sensor_data_direction_2min_no2,16).toString(10);
        int windDirection_2min_no2 = Integer.parseInt(direction_2min_no2)-22;
        if (windDirection_2min_no2 < 0){
            windDirection_2min_no2 = windDirection_2min_no2+359;
            return windDirection_2min_no2;
        }
        return windDirection_2min_no2;
    }

    /**
     * 10min风向
     */
    public static double windDirection_10min_no2(String message){
        String sensor_data_direction_10min_no2 = message.substring(66,70);
        String direction_10min_no2 = new BigInteger(sensor_data_direction_10min_no2,16).toString(10);
        int windDirection_10min_no2 = Integer.parseInt(direction_10min_no2)-22;
        if (windDirection_10min_no2 < 0){
            windDirection_10min_no2 = windDirection_10min_no2+359;
            return windDirection_10min_no2;
        }
        return windDirection_10min_no2;
    }

    /**
     *
     * 温度
     */
    public static String temperature(String message){
        String negative_positive = message.substring(70,72);//sensor_data_temperature
        String sensor_data_temperature = message.substring(72,74);
        String s1 = new BigInteger(sensor_data_temperature,16).toString(10);
        if (negative_positive.equals("00")){
            String symbol = "-";
            String centigrade = "℃";
            String temperature = symbol+s1+centigrade;
            //int temperature = Integer.parseInt(s1);
            return temperature;
        }else{
            String centigrade = "℃";
            String temperature = s1+centigrade;
            return temperature;
        }
    }

    /**
     *
     * @param message
     * @return 1号传感器的3个时间段平均风速
     */
    public static double average_speed_no1(String message){
        double averageSpeed = (windSpeed_3s_no1(message)+windSpeed_2min_no1(message)+windSpeed_10min_no1(message))/3.0;
        double average_speed_no1 = (double) Math.round(averageSpeed * 100) / 100;
        return average_speed_no1;
    }

    /**
     *
     * @param message
     * @return 1号传感器的3个时间段平均风向
     */
    public static double average_direction_no1(String message){
        double averageDirection = (windDirection_3s_no1(message)+windDirection_2min_no1(message)+windDirection_10min_no1(message))/3.0;
        double average_direction_no1 = (double) Math.round(averageDirection * 100) / 100;
        return average_direction_no1;
    }


    /**
     *
     * @param message
     * @return 2号机的3个时间段平均风速
     */
    public static double average_speed_no2(String message){
        double averageSpeed = (windSpeed_3s_no2(message)+windSpeed_2min_no2(message)+windSpeed_10min_no2(message))/3.0;
        double average_speed_no2 = (double) Math.round(averageSpeed * 100) / 100;
        return average_speed_no2;
    }

    /**
     *
     * @param message
     * @return 2号机的3个时间段平均风向
     */
    public static double average_direction_no2(String message){
        double averageDirection = (windDirection_3s_no2(message)+windDirection_2min_no2(message)+windDirection_10min_no2(message))/3.0;
        double average_direction_no2 = (double) Math.round(averageDirection * 100) / 100;
        return average_direction_no2;
    }
}
