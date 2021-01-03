package edu.xpu.cs.lovexian.common.utils;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author czy
 * @create 2020-12-03-16:49
 * 下位机的指令解析
 */
public class InstructionUtil {
    /**
     * 获取指令类型(A1|A2|...)
     *
     * @param message
     * @return
     */
    public static String getInstructionType(String message) {
        String type = message.substring(6, 8);
        return type;
    }

    /**
     * 获取指令的ACK状态
     *
     * @param message
     * @return
     */
    public static String getAck(String message) {
        String instructionType = getInstructionType(message);
        String Ack;
        Ack = message.substring(12, 14);
        return Ack;
    }

    /**
     * 传感器类型转化
     *
     * @param message
     * @return
     */
    public static String toType(String message) {
        System.out.println(message);
        if (message.equals("01")) {
            return "湿度传感器";
        }
        if (message.equals("02")) {
            return "风速传感器";
        }
        if (message.equals("03")) {
            return "水盐传感器";
        }
        return "未知传感器";
    }

    /**
     * 获取传感器数量
     *
     * @param message
     * @return
     */
    public static int getSensorNumber(String message) {
        String Sensor_Num = message.substring(16, 18);
        Integer Num = Integer.valueOf(Sensor_Num, 16);
        return Num;
    }

    /**
     * 获取传感器类型
     *
     * @param message
     * @return
     */
    public static String[] getSensorType(String message) {

        String instructionType = getInstructionType(message);
        if (instructionType.equals("A1")) {
            String[] sensorType = new String[0];
            String type = message.substring(16, 18);
            sensorType[0] = toType(type);
            return sensorType;
        }
        if (instructionType.equals("A2")) {
            int number = getSensorNumber(message);
            System.out.println("number为"+number);
            String[] sensorType = new String[number];
            for (int i = 0; i < number; i++) {
                String Sensor_Type = toType(message.substring(18 + i * 6, 20 + i * 6));
                sensorType[i] = Sensor_Type;
                System.out.println(Sensor_Type);
            }

            return sensorType;
        }
        if (instructionType.equals("A6")) {
            String[] sensorType = new String[1];
            String type = message.substring(14, 16);
            sensorType[0] = toType(type);
            return sensorType;
        }
        return null;
    }
 /*   //获取传感器的编号
    public String getSenserSerialNum(String message)
    {
        String SenserSerialNum = message.substring(14, 16);
        return SenserSerialNum;
    }*/

    /**
     * 获取数据的帧序号
     * @param message
     * @return String
     */
     public static String getFrameNum(String message){
         String frameNumHex = message.substring(4,6);
         String frameNumDec = new BigInteger(frameNumHex,16).toString(10);
         return frameNumDec;
     }

    /**
     * 获取Device_ID
     *
     * @param message
     * @return
     */
    public static String getDeviceId(String message) {
        String instructionType = getInstructionType(message);
        if (instructionType.equals("A6") || instructionType.equals("A8"))//A2,3
        {
            String deviceID = message.substring(12, 14);
            return deviceID;
        } else {
            String deviceID = message.substring(14, 16);
            return deviceID;
        }
    }

    /**
     * 获取传感器上报时间
     *
     * @param message
     * @return
     */
    public static String getTime(String message) {
        String str = message.substring(16, 20);
        String time = new BigInteger(str, 16).toString(10);
        return time;
    }

    /**
     * 获取传感器的地址
     *
     * @param message
     * @return
     */
    public static String[] getSensorAddress(String message) {
        String instructionType = getInstructionType(message);
        if (instructionType.equals("A2")) {
            int number = getSensorNumber(message);
            String[] sensorType = new String[number];
            for (int i = 0; i < number; i++) {
                String Sensor_Addr = message.substring(20 + i * 6, 24 + i * 6);
                sensorType[i] = Sensor_Addr;
            }
            return sensorType;
        }
        if (instructionType.equals("A6")) {
            String[] Sensor_Addr = new String[1];
            String Sensor_Address = message.substring(16, 20);
            Sensor_Addr[0] = Sensor_Address;
            return Sensor_Addr;
        } else return null;
    }

    /**
     * 获取数据终端设备的充电状态
     *
     * @param message
     * @return
     */
    public static String getChange(String message) {
        String instructionType = getInstructionType(message);
        String change;
        if (instructionType.equals("A8")) {
            change = message.substring(14, 16);
        } else {
            change = message.substring(16, 18);
        }
        if (change.equals("01"))
            return "处于充电状态";
        else return "未处于充电状态";
    }

    /**
     * 获取数据终端设备的电池容量
     *
     * @param message
     * @return
     */
    public static float getBatteryLevel(String message) {
        String instructionType = getInstructionType(message);
        if (instructionType.equals("A8")) {
            String battery = message.substring(16, 20);
            String batteryLevel = new BigInteger(battery, 16).toString();
            float BatteryLevel = Float.parseFloat(batteryLevel);
            return BatteryLevel;
        } else {
            String battery = message.substring(18, 22);
            String batteryLevel = new BigInteger(battery, 16).toString();
            float BatteryLevel = Float.parseFloat(batteryLevel);
            return BatteryLevel;
        }
    }

    /**
     * String明文(16进制)转ASCII码hex字符串
     *
     * @param str
     * @return
     */
    public static String stringToHex(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 这里的第二个参数16表示十六进制
            sb.append(Integer.toString(c, 16));
            // 或用toHexString方法直接转成16进制
            // sb.append(Integer.toHexString(c));
        }
        return sb.toString();
    }

    /**
     * String明文(16进制)转ASCII码hex字符串
     *
     * @param hex
     * @return
     */
    public static String hexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            String h = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(h, 16);
            sb.append((char) decimal);
        }
        return sb.toString();
    }


    /**
     * 获取A6的传感器数据
     */
    public static String getSensorData(String message) {
        String s0 = message.substring(20, 22);
        String len = new BigInteger(s0, 16).toString(10);
        int length = Integer.valueOf(len);
        String s = message.substring(22, 22 + 2 * length);
        String sensorsData = s;
        return sensorsData;

    }

    public static int getDataLength(String message) {
        int length = message.substring(0, message.length() - 4).length();
        return length;
    }

    public static int getEqDuration(Date createTime, Date updateTime) {
        long t1 = updateTime.getTime();
        long t2 = createTime.getTime();
        //3.时间差
        int day = (int) ((t1 - t2) / (1000 * 60 * 60 * 24));
        return day;
    }

    public static String getTbale(String settingId) {
        if (settingId.length() == 5)
            return "dtus";
        if (settingId.length() == 4)
            return "gateways";
        return "sensors";

    }

    public static String getColum(String settingId) {
        switch (getTbale(settingId)) {
            case "dtus":
                return "dtu_id";
            case "gateways":
                return "gate_id";
            case "sensors":
                return "sensor_id";
            default:
                return null;
        }
    }

    public static String getType(String settingId) {
        switch (getTbale(settingId)) {
            case "dtus":
                return "1";
            case "sensors":
                return "0";
            case "gateways":
                return "2";
            default:
                return null;
        }
    }
}
