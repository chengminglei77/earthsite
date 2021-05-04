package edu.xpu.cs.lovexian.common.utils;

import java.math.BigInteger;

/**
 *author:zhanganjie
 *
 */
public class WindSpeedUtils {
    /**
     * 1号设备和2号设备三个时间段的平均风速
     * 现在修改为一次只发一个设备的数据
     */
    private static final String[] deviceNum = {"01", "02"};//此时数据是分开发了deviceNum.length=1
    private static final String[][] windSpeedString = new String[2][3];//表示两个设备，每个设备又对应三个不同的风速
    private static double[][] windSpeedDouble = new double[2][3];//表示两个设备，每个设备又对应三个不同的风速
    private static double[] averageWindSpeed = new double[1];//表示两个设备分别对应的风速，这里如果以后数据分开发，就把2改为1

    public static double[] windSpeed(String message) {
        for (int j = 0; j < 3; j++) {
            windSpeedString[0][j] = new BigInteger(message.substring(22 + 8 * j, 26 + 8 * j), 16).toString(10);
            //windSpeedString[1][j] = new BigInteger(message.substring(46+8*j,50+8*j),16).toString(10);

        }
        for (int k = 0; k < 3; k++) {
            windSpeedDouble[0][k] = Double.valueOf(windSpeedString[0][k]);//现在一条数据就只有一个传感器的数据了
            //windSpeedDouble[1][k] = Double.valueOf(windSpeedString[1][k]);
        }
        for (int i = 0; i < averageWindSpeed.length; i++) {
            double sum = 0.0;
            for (int m = 0; m < 3; m++) {
                sum += windSpeedDouble[i][m];
            }
            averageWindSpeed[i] = sum / 300.0;
        }
        return averageWindSpeed;
    }

    /**
     * 传感器的方向
     */
    private static final double[] windDirection_10min_no = new double[1];//修改为1，后期若多个设备整合一条数据发过来，只需修改数组长度
    private static final String[] sensor_data_direction_10min_no = new String[1];//同上
    private static final String[] direction_10min_no = new String[1];//同上
    private static final String[] directionNum = new String[1];//01或者02现在有两种类型的风向传感器，只不过不是在一条数据里了，分开发
    private static final double[] Direction = {0, 90, 180, 270, 359};
    private static final String[] DirectionS = {"北", "东北", "东", "东南", "南", "西南", "西", "西北"};

    public static String[] windDirection(String message) {
        String[] direction = new String[1];//只有一个设备改为1
        directionNum[0] = message.substring(6, 8);
        //directionNum[1] = message.substring(66,70);

        for (int i = 0; i < windDirection_10min_no.length; i++) {
            sensor_data_direction_10min_no[i] = message.substring(42, 46);
            direction_10min_no[i] = new BigInteger(sensor_data_direction_10min_no[i], 16).toString(10);
        }
        //判断是几号设备
        /*
        1号机：在所测风向上 +92°        位置： 位于架子的 右侧
        2号机：在所测风向上 -22°		  位置： 位于架子的 左侧
         */
        if (message.substring(18, 20).equals("01")) {
            windDirection_10min_no[0] = Double.valueOf(direction_10min_no[0]) + 92;
            if (windDirection_10min_no[0] > 359) {
                windDirection_10min_no[0] = windDirection_10min_no[0] - 359;
            }
        } else {
            windDirection_10min_no[0] = Double.valueOf(direction_10min_no[0]) - 22;
            if (windDirection_10min_no[0] < 0) {
                windDirection_10min_no[0] = windDirection_10min_no[0] + 359;
            }
        }


        for (int i = 0; i < directionNum.length; i++) {
            if (0 <= windDirection_10min_no[i] && windDirection_10min_no[i] <= 359) {
                for (int k = 0; k < 4; k++) {
                    if (windDirection_10min_no[i] == Direction[k]) {
                        direction[i] = DirectionS[2 * k];
                    }
                }
                for (int j = 0; j < 4; j++) {
                    if (Direction[j] < windDirection_10min_no[i] && windDirection_10min_no[i] < Direction[j + 1]) {
                        direction[i] = DirectionS[2 * j + 1];
                    }
                }
            }
        }
        return direction;
    }


    /**
     * 温度
     */
    public static String temperature(String message) {
        String negative_positive = message.substring(48, 50);//sensor_data_temperature
        String sensor_data_temperature = message.substring(50, 52);
        String s1 = new BigInteger(sensor_data_temperature, 16).toString(10);
        if (negative_positive.equals("00")) {
            String symbol = "-";
            String centigrade = "℃";
            String temperature = symbol + s1 + centigrade;
            //int temperature = Integer.parseInt(s1);
            return temperature;
        } else {
            String centigrade = "℃";
            String temperature = s1 + centigrade;
            return temperature;
        }
    }


    /**
     * 报警信息
     *
     * @param
     */
    public static String alarmInfo(String message) {
        String alarmInfo = null;
        String info = message.substring(46, 48);
        if (info.equals("00")) {
            //System.out.println("正常");
            alarmInfo = "正常";
        }
        if (info.equals("01")) {
            //System.out.println("设备工作状态异常");
            alarmInfo = "设备工作状态异常";
        }
        if (info.equals("02")) {
            alarmInfo = "数据异常";
        }
        return alarmInfo;
    }

    public static void main(String[] args) {
        String test = "AA55F9A60014010200010F00140000001600420014004B0001A0";
        double [] weedSpeed = windSpeed(test);
        for (int i = 0; i < weedSpeed.length; i++) {
            System.out.println(weedSpeed[i]);
        }
        String [] windDirection = windDirection(test);
        for (int i = 0; i < weedSpeed.length; i++) {
            System.out.println(windDirection[i]);
        }
        String wendu = temperature(test);
        System.out.println(wendu);
        String checkNum = alarmInfo(test);
        System.out.println(checkNum);
    }
}
