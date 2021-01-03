package edu.xpu.cs.lovexian.common.utils;

import java.math.BigInteger;

/**
 * @author:zhanganjie
 */
public class WindSpeedUtils {
    /**
     * 解码风速传感器的工具类
     * @param message
     * @return
     */

    /**
     * 1号设备和2号设备三个时间段的平均风速
     */
    private static final String [] deviceNum = {"00","01"};
    private static final String [][] windSpeedString = new String[2][3];
    private static double [][] windSpeedDouble = new double[2][3];
    private static double [] averageWindSpeed = new double[2];
    public static double[] windSpeed(String message){
        for (int j=0;j<3;j++){
            windSpeedString[0][j] = new BigInteger(message.substring(22+8*j,26+8*j),16).toString(10);
            windSpeedString[1][j] = new BigInteger(message.substring(46+8*j,50+8*j),16).toString(10);

        }
        for (int k=0;k<3;k++){
            windSpeedDouble[0][k] = Integer.parseInt(windSpeedString[0][k]);
            windSpeedDouble[1][k] = Integer.parseInt(windSpeedString[1][k]);
        }
        for (int i=0;i<averageWindSpeed.length;i++){
            double sum = 0.0;
            for (int m=0;m<3;m++){
                sum += windSpeedDouble[i][m];
            }
            averageWindSpeed[i] = sum/3.0;
        }
        return averageWindSpeed;
    }

    /**
     * 传感器的方向
     */
    private static final double[] windDirection_10min_no = new double[2];
    private static final String[] sensor_data_direction_10min_no = new String[2];
    private static final String[] direction_10min_no = new String[2];
    private static final String[] directionNum = {"01","02"};
    private static final double [] Direction = {0,90,180,270,359};
    private static final String [] DirectionS ={"北","东北","东","东南","南","西南","西","西北"};
    public static String[] windDirection(String message){
        String [] direction= new String[2];
        directionNum[0] = message.substring(42,46);
        directionNum[1] = message.substring(66,70);

        for (int i=0;i<windDirection_10min_no.length;i++){
            sensor_data_direction_10min_no[i] = message.substring(42+24*i,46+24*i);
            direction_10min_no[i] = new BigInteger(sensor_data_direction_10min_no[i],16).toString(10);
        }
        windDirection_10min_no[0] = Double.valueOf(direction_10min_no[0])+92;
        windDirection_10min_no[1] = Double.valueOf(direction_10min_no[1])-22;

        if (windDirection_10min_no[0] > 359){
            windDirection_10min_no[0] = windDirection_10min_no[0]-359;
        }
        if (windDirection_10min_no[1] < 0){
            windDirection_10min_no[1] = windDirection_10min_no[1]+359;
        }

        for (int i=0;i<directionNum.length;i++){
            if (0<=windDirection_10min_no[i]&&windDirection_10min_no[i]<=359){
                for (int k=0;k<4;k++){
                    if (windDirection_10min_no[i] == Direction[k]){
                        direction[i] = DirectionS[2*k];
                    }
                }
                for (int j=0;j<4;j++){
                    if (Direction[j]<windDirection_10min_no[i]&&windDirection_10min_no[i]<Direction[j+1]){
                        direction[i] =  DirectionS[2*j+1];
                    }
                }
            }
        }
        return direction;
    }
}
