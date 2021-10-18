package edu.xpu.cs.lovexian.common.utils;

import java.math.BigInteger;
import java.util.*;

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
     * 获取传感器10min时的风速
     * @return
     */
    public static double get10minWindSpeed(String message){
        double WindSpeed10min = windSpeedDouble[0][2]/100.0;
        return WindSpeed10min;
    }

    /**
     * 传感器的方向
     * 外墙01号机（标准高度135cm）
     * 01号传感器高度 -115cm ，矫正角度： 180°
     * 02号传感器高度 290cm,   矫正角度： 180°
     * 内墙00号机（标准高度113cm）
     * 01号传感器高度87cm， 不需要角度矫正。
     * 02号传感器高度287cm，不需要角度矫正。
     * 2m杆距离墙面5m
     * 外墙03号机
     * 01号传感器高度16cm ，矫正角度： 31°
     * 02号传感器高度155cm, 矫正角度： 159°
     * 内墙02号机
     * 01号传感器高度15cm，矫正角度： -40°
     * 02号传感器高度90cm，矫正角度： -20°
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
        /**
         * 外墙01号机（标准高度135cm）
         */
        if (message.substring(16,18).equals("01")){
            windDirection_10min_no[0] = Double.valueOf(direction_10min_no[0]) + 180;
            if (windDirection_10min_no[0] > 360) {
                windDirection_10min_no[0] = windDirection_10min_no[0] - 360;
            }
        }

        /**
         * 外墙03号机
         */
        if (message.substring(16,18).equals("03")){
            if (message.substring(18,20).equals("01")){
                windDirection_10min_no[0] =  Double.valueOf(direction_10min_no[0]) + 31;
            }else {
                windDirection_10min_no[0] =  Double.valueOf(direction_10min_no[0]) + 159;
            }
            if (windDirection_10min_no[0] > 360) {
                windDirection_10min_no[0] = windDirection_10min_no[0] - 360;
            }
        }

        /**
         * 内墙02号机
         */
        if (message.substring(16,18).equals("02")){
            if (message.substring(18,20).equals("01")){
                windDirection_10min_no[0] =  Double.valueOf(direction_10min_no[0]) - 40;
            }else {
                windDirection_10min_no[0] =  Double.valueOf(direction_10min_no[0]) - 20;
            }
            if (windDirection_10min_no[0] < 0) {
                windDirection_10min_no[0] = windDirection_10min_no[0] + 360;
            }
        }

        if (windDirection_10min_no[0] == 360 ){
            windDirection_10min_no[0] = 0.0;
        }
        /**
         * 内墙00号机（标准高度113cm）
         * 不需要矫正
         */
        if (message.substring(16,18).equals("00")){
            windDirection_10min_no[0] = Double.valueOf(direction_10min_no[0]);
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
        System.out.println(windDirection_10min_no[0]);
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

    /**统计风速次数
     * 风速计数器0  ：  采样间隔内风速位于 6m/s—8m/s   的次数，计数范围 0- 200
     * 风速计数器1  ：  采样间隔内风速位于 8m/s—10m/s  的次数，计数范围 0- 200
     * 风速计数器2  ：  采样间隔内风速位于 10m/s—12m/s 的次数，计数范围 0- 200
     * 风速计数器3  ：  采样间隔内风速位于 12m/s—14m/s 的次数，计数范围 0- 200
     * 风速计数器4  ：  采样间隔内风速位于 14m/s—16m/s 的次数，计数范围 0- 200
     * 风速计数器5  ：  采样间隔内风速位于 16m/s—18m/s 的次数，计数范围 0- 200
     * @param message
     * @return返回一个长度为6的传感器计数的数组
     */
    private static final String [] count = new String[6];
    private static final String [] decodeCount = new String[6];
    public static String[] windSpeedCount(String message){
        WindSpeedUtils windSpeedUtils = new WindSpeedUtils();
        List<String> list = new ArrayList<>();
        String windSpeedCount = A6Utils.getWindSpeedCount(message);
        System.out.println(windSpeedCount);
        for(int i=0;i<windSpeedCount.length();i++){
            String o = Integer.toString(i);
            //list.add(windSpeedCount.substring(48,50));
        }
        for (int j=0;j<6;j++){
            count[j] = message.substring(48+2*j,50+2*j);
            decodeCount[j] = new BigInteger(count[j],16).toString();
        }
        //count = windSpeedUtils.splitList(list,2);
        return decodeCount;
    }


    /**
     * 数组分割算法,由风速次数统计调用
     * @param list
     * @param groupSize
     * @return
     */
    private List<List<String>> splitList(List<String> list , int groupSize){
        int length = list.size();
        // 计算可以分成多少组
        int num = ( length + groupSize - 1 )/groupSize ; // TODO
        List<List<String>> newList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            // 开始位置
            int fromIndex = i * groupSize;
            // 结束位置
            int toIndex = (i+1) * groupSize < length ? ( i+1 ) * groupSize : length ;
            newList.add(list.subList(fromIndex,toIndex)) ;
        }
        return  newList ;
    }

    public static void main(String[] args) {
        String test = "AA55DBA6001A0402030215003C014B0039015A00250155000000000000000102624E55AA0D0A";
        String test1 = "AA55F7A6001A0402010115001F014800360002003A016200011000000000010C952155AA0D0A";
        //System.out.println(A6Utils.getDataLen(test)*2);//现在的风速传感器长度为0014,40位
        //System.out.println(Integer.valueOf(A6Utils.getSensorDataLen(test))*2);//风速数据长度为0F，30位
        double [] speed = windSpeed(test);
        String [] s = windSpeedCount(test);
        String[] f = windDirection(test);
        for (int i=0;i<1;i++){
            System.out.println(f[i]);
            System.out.println(speed[i]);
        }

        //System.out.println(s);
        for (int i=0;i<6;i++){
            System.out.println(s[i]);
        }
        //String checkNum = alarmInfo(test);
        //System.out.println(checkNum);
    }
}
