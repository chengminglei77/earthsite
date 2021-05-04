package edu.xpu.cs.lovexian.common.utils;

import java.math.BigInteger;

/**
 * @author zaj
 * 新的A6工具类
 */
//23 02 00 01 0F 00 14 00 00 00 16 00 42 00 14 00 4B 00 01 A0 B2 21
public class A6Utils {

    /**
     * 获取数据长度
     * @param message
     * @return
     */
    public static int getDataLen(String message){
        String len = message.substring(8,12);
        int dataLen = Integer.valueOf(new BigInteger(len,16).toString(10));
        return dataLen;
    }
    /**
     * 传感器类型
     * @param message
     * @return
     */
    public static String getSensorType(String message){
        String sensorType = null;
        String sensorTypeNum = message.substring(14,16);
        if (sensorTypeNum.equals("01")){
            sensorType = "湿度传感器";
        }
        if (sensorTypeNum.equals("02")){
            sensorType = "风速传感器";
        }
        if (sensorTypeNum.equals("03")){
            sensorType = "水盐传感器";
        }
        return sensorType;
    }

    /**
     * 盒子编号
     * @param message
     * @return
     */
    public static String getBoxNum(String message){
        String sensorBoxNum = message.substring(16,18);//地址高位代表盒子的编号，低位表示该编号盒子下所连接的传感器编号
        return sensorBoxNum;
    }

    /**
     * 设备编号
     * @param message
     * @return
     */
    public static String getSensorNum(String message){
        String sensorNum = message.substring(18,20);
        return sensorNum;
    }

    /**
     * 获取数据长度
     * @param message
     * @return
     */
    public static String getSensorDataLen(String message){
        String sensorDataLen = message.substring(20,22);
        return sensorDataLen;
    }

    /**
     * 传感器数据
     * @param message
     * @return
     */
    public static String getSensorData(String message){
        String len = new BigInteger(getSensorDataLen(message),16).toString(10);
        int dataLen = Integer.valueOf(len);
        String sensorData = message.substring(22,22+2*dataLen);
        return sensorData;
    }

    /**
     * 校验
     * @param message
     * @return
     */
    public static String getCheckNum(String message){
        String checkNum = message.substring(46,48);
        return checkNum;
    }
    public static void main(String[] args) {
        String testNum = "AA55F9A60014010200010F00140000001600420014004B0001A0";
        int dataLen = getDataLen(testNum);
        String sensorType = getSensorType(testNum);
        String boxNum = getBoxNum(testNum);
        String sensorNum = getSensorNum(testNum);
        String sensorDataLen = getSensorDataLen(testNum);
        String sensorData = getSensorData(testNum);
        String checkNum = getCheckNum(testNum);
        System.out.println("data_len:"+dataLen);
        System.out.println("sensorType:"+sensorType);
        System.out.println("boxNum:"+boxNum);
        System.out.println("sensorNum:"+sensorNum);
        System.out.println("sensorDataLen:"+sensorDataLen);
        System.out.println("sensorData:"+sensorData);
        System.out.println("checkNum:"+checkNum);
    }
}
