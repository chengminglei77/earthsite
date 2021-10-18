package edu.xpu.cs.lovexian.common.utils;

import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.UUID;

/**
 * @author zaj
 * 新的A6风速工具类
 */
//23 02 00 01 0F 00 14 00 00 00 16 00 42 00 14 00 4B 00 01 A0 B2 21
public class A6Utils {

    //AA55F7A6001A0402030215001F014800360002003A016200000000000000010C952155AA0D0A
    /**
     * 帧头：AA55
     * F7A6001A
     * DTU型号：04
     * 传感器类型：02
     * 传感器编号：0302
     * 数据长度：15
     * 风速风向：001F014800360002003A0162
     * 数据异常位：00
     * 风速计数位：000000000000
     * 温度标志位：01
     * 温度：0C
     * 校验位：952155AA
     * 帧尾：0D0A
     */
    private static Logger logger = Logger.getLogger(A6Utils.class);

    /**
     * 获取数据长度
     * @param message
     * @return字节长度
     */
    public static int getDataLen(String message){
        String len = message.substring(8,12);
        int dataLen = Integer.valueOf(new BigInteger(len,16).toString(10));
        return dataLen;
    }

    /**
     * 获取DTU的类型
     * @param message
     * @return
     */
    public static String dtuType(String message){
        return message.substring(12,14);
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
        if (!sensorTypeNum.equals("01") && !sensorTypeNum.equals("02") && !sensorTypeNum.equals("03"))
        {
            sensorType = "未知传感器"+sensorTypeNum;
            logger.info("找不到对应的传感器，请检查数据是否有误");
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
        String sensorNum = message.substring(16,20);
        return sensorNum;
    }


    /**
     * 获取数据长度
     * @param message
     * @return字节长度
     */
    public static String getSensorDataLen(String message){
        String sensorDataLen = message.substring(20,22);
        String len = new BigInteger(sensorDataLen,16).toString(10);
        return len;
    }

    /**
     * 传感器数据
     * @param message
     * @return字节长度
     */
    public static String getSensorData(String message){
        String len = getSensorDataLen(message);
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
        String alarmInfo = null;
        String len = getSensorDataLen(message);
        int dataLen = Integer.valueOf(len);
        String checkNum = message.substring(46,48);
        if (checkNum.equals("00")) {
            //System.out.println("正常");
            alarmInfo = "正常";
        }
        if (checkNum.equals("01")) {
            //System.out.println("设备工作状态异常");
            alarmInfo = "设备工作状态异常";
        }
        if (checkNum.equals("02")) {
            alarmInfo = "传感器数据异常";
        }
        logger.info(alarmInfo);
        return checkNum;
    }


    /**
     * 获取风速计数，总共六组每组一个字节，两位;如00 00 00 00 00 00
     * @param message
     * @return
     */
    public static String getWindSpeedCount(String message){
        String len = getSensorDataLen(message);
        int dataLen = Integer.valueOf(len);
        return message.substring(48,60);
    }

    /**
     * 温度标志位
     * @param message
     * @return
     */
    public static String getTemperatureMark(String message){
        String symbol = null;
        String mark = message.substring(60,62);
        if (mark.equals("00")){
            symbol = "-";
        }else{
            symbol = "";
        }
        return symbol;
    }

    /**
     * 得到温度值
     * @param message
     * @return
     */
    public static String getTemperature(String message){
        String centigrade = "℃";
        String sensor_data_temperature = message.substring(62, 64);
        String temperature = new BigInteger(sensor_data_temperature, 16).toString(10);
        return getTemperatureMark(message) + temperature + centigrade;
    }
    /**
     * 生成UID
     * @return
     */
    public static String getUUID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        return str;
    }

    public static void main(String[] args) {
        String testNum = "AA55F7A6001A0402030215001F014800360002003A016200000000000000010C952155AA0D0A";
        int dataLen = getDataLen(testNum);
        String sensorType = getSensorType(testNum);
        String boxNum = getBoxNum(testNum);
        String sensorNum = getSensorNum(testNum);
        String sensorDataLen = getSensorDataLen(testNum);
        String sensorData = getSensorData(testNum);
        String checkNum = getCheckNum(testNum);
        String windSpeedCount = getWindSpeedCount(testNum);
        String temperature = getTemperature(testNum);
        System.out.println("data_len:"+dataLen);
        System.out.println("sensorType:"+sensorType);
        System.out.println("boxNum:"+boxNum);
        System.out.println("sensorNum:"+sensorNum);
        System.out.println("sensorDataLen:"+sensorDataLen);
        System.out.println("sensorData:"+sensorData);
        System.out.println("checkNum:"+checkNum);
        System.out.println(getUUID());
        //String sensorType = getSensorType(testNum);
        System.out.println("windSpeedCount:"+windSpeedCount);
        System.out.println("temperature:"+temperature);
    }
}
