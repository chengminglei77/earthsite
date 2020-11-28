package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensorsData;
import edu.xpu.cs.lovexian.app.appadmin.mapper.SensorsDataAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.ISensorsDataAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author czy
 * @create 2020-11-27-10:20
 */
@Service("sensorsDataAdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SensorsDataAdminServiceImpl extends ServiceImpl<SensorsDataAdminMapper, AdminSensorsData> implements ISensorsDataAdminService {
    @Autowired
    private SensorsDataAdminMapper sensorsDataAdminMapper;

    @Override
    public String setSensorAddrAndType(String message){
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            String h = message.substring(6, 8);
            if (h.equals("A1")){
                String ACK = message.substring(12, 14);
                if (ACK.equals("01"))
                    return "失败";
                if (ACK.equals("02"))
                    return "CRC校验失败";
                if(ACK.equals("00")) {
                    AdminSensorsData data = new AdminSensorsData();
                    String Device_ID = message.substring(14, 16);
                    data.setDeviceId(Device_ID);
                    sensorsDataAdminMapper.insert(data);
                    return "成功";
                }else return "错误";
            }else return "错误";
        }else return "错误";
    }
    @Override
    public String reportSensorAddrAndTypeAndNum(String message){
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            String h = message.substring(6, 8);
            if (h.equals("A2")){
                String ACK = message.substring(12, 14);
                if (ACK.equals("01"))
                    return "失败";
                if (ACK.equals("02"))
                    return "CRC校验失败";
                if(ACK.equals("00")) {
                    AdminSensorsData data = new AdminSensorsData();
                    String Device_ID = message.substring(14, 16);
                    data.setDeviceId(Device_ID);
                    String Sensor_Num = message.substring(16, 18);
                    Integer Num = Integer.valueOf(Sensor_Num, 16);
                    //System.out.println(Num);
                    data.setSensorNum(Num);
                    for (int N=0;N<Num;N++) {
                        String Sensor_Type = message.substring(18+N*6, 20+N*6);
                        Integer Typ = Integer.valueOf(Sensor_Type, 16);
                        data.setSensorType(Typ);
                        String Sensor_Addr = message.substring(20+N*6, 24+N*6);
                        data.setSensorAddr(Sensor_Addr);
                    }
                    sensorsDataAdminMapper.insert(data);
                    return "成功";
                }else return "错误";
            }else return "错误";
        }else return "错误";
    }
    @Override
    public String deleteSensor(String message){
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            String h = message.substring(6, 8);
            if (h.equals("A3")){
                String ACK = message.substring(12, 14);
                if (ACK.equals("01"))
                    return "失败";
                if (ACK.equals("02"))
                    return "CRC校验失败";
                if(ACK.equals("00")) {
                    AdminSensorsData data = new AdminSensorsData();
                    String Device_ID = message.substring(14, 16);
                    data.setDeviceId(Device_ID);
                    sensorsDataAdminMapper.insert(data);
                    return "成功";
                }else return "错误";
            }else return "错误";
        }else return "错误";
    }

    @Override
    public String setSensorReportTime(String message) {
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            String h = message.substring(6, 8);
            /**
             * (4)	设置传感器上报数据时间：0xA4
             */
            if (h.equals("A4")){
                String CRC = message.substring(12,14);
                if (CRC.equals("00")){
                    String deviceId = message.substring(14,16);
                    /*AdminSensorsData data = new AdminSensorsData();
                    data.setDeviceId(deviceId);
                    sensorsDataAdminMapper.insert(data);*/
                    return "成功";
                }else {
                    if (CRC.equals("01")){
                        return "失败";
                    }else {
                        return "CRC校验失败";
                    }
                }
            }
        }
        return "错误";
    }

    @Override
    public String getSensorReportTime(String message) {
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            String h = message.substring(6, 8);
            /**
             * (5)	获取传感器上报数据时间：0xA5
             */
            if (h.equals("A5")){
                Date date = new Date();
                String ACK = message.substring(12,14);
                if (ACK.equals("00")){
                    AdminSensorsData data = new AdminSensorsData();
                    String time = "";
                    String deviceId = message.substring(14,16);
                    String str = message.substring(16,20);
                    String sub = new BigInteger(str, 16).toString(10);
                    time = sub;
                    data.setTime(time);
                    sensorsDataAdminMapper.insert(data);
                    return "成功";
                }else {
                    if (ACK.equals("01")){
                        return "失败";
                    }else {
                        if (ACK.equals("02")){
                            return "校验失败";
                        }
                    }
                }
            }
        }
        return "错误";
    }

    @Override
    public String Reportsensordatacommand(String message) {
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            /**
             * (6)	上报传感器数据指令：0xA6
             */
            String h = message.substring(6, 8);
            if (h.equals("A6")) {
                String deviceId = message.substring(12, 14);
                String sensorType = message.substring(14, 16);
                String sensorAddr = message.substring(16, 20);
                AdminSensorsData data = new AdminSensorsData();
                if (sensorType.equals("01")) {
                    sensorType = "湿度传感器";
                } else {
                    if (sensorType.equals("02")) {
                        sensorType = "风速传感器";
                    } else {
                        if (sensorType.equals("03")) {
                            sensorType = "水盐传感器";
                        }
                    }
                }
                data.setSensorType(sensorType);
                String sensorDataLen = message.substring(20,22);
                int len = Integer.valueOf(message.substring(20,22));
                data.setSensorDataLen(sensorDataLen);
                String sensorData = message.substring(22,22+2*len);
                data.setSensorData(sensorData);
                sensorsDataAdminMapper.insert(data);
                return "成功";
            }
        }
        return "错误";
    }

    @Override
    public String querySensorAdress(String message) {
        //String message="AA550A0700010255AA";
        //StringBuilder sb = new StringBuilder();
        int i;
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            String h = message.substring(6, 8);
            if (h.equals("A7")) {
                String s = message.substring(12, 14);
                if (s.equals("01"))
                    return "失败";
                if (s.equals("02"))
                    return "CRC校验失败";
                if (s.equals("00")) {
                    AdminSensorsData data = new AdminSensorsData();
                    data.setDeviceId(s);
                    sensorsDataAdminMapper.insert(data);
                    return "成功";
                    //bookList.add(book);
                } else return "null";
            }
            if (h.equals("A8")) {
                AdminSensorsData data = new AdminSensorsData();
                String s = message.substring(12, 14);
                data.setDeviceId(s);
                String s1 = message.substring(14, 18);
                String sub = new BigInteger(s1, 16).toString(10);
                data.setBatteryLevel(sub);
                sensorsDataAdminMapper.insert(data);
                return "成功";
            }
            if (h.equals("A9")) {
                String s = message.substring(12, 14);
                if (s.equals("01"))
                    return "失败";
                if (s.equals("02"))
                    return "CRC校验失败";
                if (s.equals("00")) {
                    AdminSensorsData data = new AdminSensorsData();
                    String s1 = message.substring(14, 16);
                    data.setDeviceId(s1);
                    String s2 = message.substring(16, 20);
                    String sub = new BigInteger(s2, 16).toString(10);
                    data.setBatteryLevel(sub);
                    sensorsDataAdminMapper.insert(data);
                    return "成功";
                } else return "错误";
            } else return "错误";
        }else return "错误";
    }

    public static void main(String[] args) {
        SensorsDataAdminServiceImpl se = new SensorsDataAdminServiceImpl();
        se.querySensorAdress("AA5500A2000600AA0201AACC55AA");
    }
}