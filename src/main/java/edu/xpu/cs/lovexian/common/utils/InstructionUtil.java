package edu.xpu.cs.lovexian.common.utils;

import edu.xpu.cs.lovexian.app.appadmin.Kafka.PerformInstrution;
import jdk.nashorn.api.scripting.ScriptUtils;
import org.jcp.xml.dsig.internal.dom.DOMUtils;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
            System.out.println("number为" + number);
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

    public static int getDayEqDuration(Date fromDate, Date toDate) {
        int days = 0;
        long time1 = fromDate.getTime();
        long time2 = toDate.getTime();

        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        days = (int) (diff / (24 * 60 * 60 * 1000));
        return days;
    }
    public static String getEqDuration(int day)
    {
        return day/365+"年"+(day%365)/30+"月"+(day%365)%30+"天";
    }

    public static String getTbale(String settingId) {
        if (settingId.length() == 9)
            return "dtus";
        if (settingId.length() == 4)
            return "gateways";
        return "sensors";

    }
    public static int getdayEqDuration(String message)
    {
        int indexOfDay=message.indexOf("天");
        int indexOfMonth=message.indexOf("月");
        return Integer.parseInt(message.substring(indexOfMonth+1,indexOfDay));

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

    public static String countTransfer(int count) {
        if (count > 0 && count < 10000)
            return count + "条";
        if (count > 10000 && count < 100000000)
            return (count / 10000) + "万" + (count % 10000) + "条";
        return (count / 100000000) + "亿" + ((count % 100000000) / 10000) + "万" + ((count % 100000000) % 10000) + "条";
    }

    public static int transferCount(String count) {
        int billion, tenThousand, one;
        int indexOfTenThousand, indexOfBillion, indexOfOne;
        indexOfBillion = count.indexOf("亿");
        if (indexOfBillion != -1) {
            billion = Integer.parseInt(count.substring(0, indexOfBillion));
            indexOfTenThousand = count.indexOf("万");
            if (indexOfTenThousand != -1) {
                tenThousand = Integer.parseInt(count.substring(indexOfBillion + 1, indexOfTenThousand));
                indexOfOne = count.indexOf("条");
                if (indexOfTenThousand + 1 != indexOfOne) {
                    one = Integer.parseInt(count.substring(indexOfTenThousand + 1, indexOfOne));
                    return billion * 100000000 + tenThousand * 10000 + one;
                }
                return billion * 100000000 + tenThousand * 10000;
            }
            indexOfOne = count.indexOf("条");
            if (indexOfBillion + 1 != indexOfOne)
            {  one = Integer.parseInt(count.substring(indexOfBillion + 1, indexOfOne));
            return billion * 100000000+one ;
        }return billion*100000000;
        }
        indexOfTenThousand = count.indexOf("万");
        if (indexOfTenThousand != -1) {
            tenThousand = Integer.parseInt(count.substring(indexOfBillion + 1, indexOfTenThousand));
            indexOfOne = count.indexOf("条");
            if (indexOfTenThousand + 1 != indexOfOne) {
                one = Integer.parseInt(count.substring(indexOfTenThousand + 1, indexOfOne));
                return tenThousand * 10000 + one;
            }
            return tenThousand * 10000;
        }
        indexOfOne = count.indexOf("条");
        one = Integer.parseInt(count.substring(0, indexOfOne));
        return one;
    }

    public static String getPrintSize(long fileS)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize="0B";
        if(fileS==0){
            return wrongSize;
        }
        if (fileS < 1024){
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576){
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        }
        else if (fileS < 1073741824){
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }
        else{
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    //返回Byte数目
    public static int toSize(String message) {
        double GB, KB, MB, B;
        int indexOfGB, indexOfKB, indexOfMB, indexOfB;
        indexOfGB = message.indexOf("GB");
        if (indexOfGB != -1) {
            GB = Double.parseDouble(message.substring(0, indexOfGB));
            return (int) (GB * 1024 * 1024 * 1024);
        }
        indexOfMB = message.indexOf("MB");
        if (indexOfMB != -1) {
            MB = Double.parseDouble(message.substring(0, indexOfMB));
            return (int) (MB * 1024* 1024);
        }
        indexOfKB = message.indexOf("KB");
        if (indexOfKB != -1) {
            KB = Double.parseDouble(message.substring(0, indexOfKB));
            return (int) (KB * 1024 );
        }

        indexOfB = message.indexOf("B");
        B = Double.parseDouble(message.substring(0, indexOfB));
        return (int) B;
    }

    public static void main(String[] args) throws ParseException {
        //设置Date格式为“年-月-日 小时:分钟:秒.毫秒”
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//设置时间，String转为Date
        String strStart = "2017-2-11 11:11:50.5";
        String strEnd = "2019-1-11 11:11:50.555";
        Date dateStart = sdf.parse(strStart);
        Date dateEnd = sdf.parse(strEnd);    //查询的数据时间
        int str=InstructionUtil.getDayEqDuration(dateStart,dateEnd);
        System.out.println(str);
        System.out.println(InstructionUtil.getEqDuration(str));
    }
}
