package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.controller.InfluxDBContoller;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtus;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensorsData;
import edu.xpu.cs.lovexian.app.appadmin.mapper.SensorsDataAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IDtusAdminService;
import edu.xpu.cs.lovexian.app.appadmin.service.ISensorsDataAdminService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service("sensorsDataAdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SensorsDataAdminServiceImpl extends ServiceImpl<SensorsDataAdminMapper, AdminSensorsData> implements ISensorsDataAdminService {
    @Autowired
    private SensorsDataAdminMapper sensorsDataAdminMapper;
    @Autowired
    private IDtusAdminService iDtusAdminService;
    @Autowired
    private InfluxDBContoller influxDBContoller;
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
                        if (Sensor_Type.equals("01")) {
                            Sensor_Type = "湿度传感器";
                        } else {
                            if (Sensor_Type.equals("02")) {
                                Sensor_Type = "风速传感器";
                            } else {
                                if (Sensor_Type.equals("03")) {
                                    Sensor_Type = "水盐传感器";
                                }
                            }
                        }
                        data.setSensorType(Sensor_Type);
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
    public void setSensorReportTime(String message) {
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
                    System.out.println("成功");
                }else {
                    if (CRC.equals("01")){
                        System.out.println("失败");
                    }else {
                        System.out.println("CRC校验失败");
                    }
                }
            }
        }
    }

    @Override
    public void getSensorReportTime(String message) {
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
                }else {
                    if (ACK.equals("01")){
                        System.out.println( "失败");
                    }else {
                        if (ACK.equals("02")){
                            System.out.println("校验失败");
                        }
                    }
                }
            }
        }
    }


    @Override
    public void ReportSensorDataCommand(String message) {
       /* if (message.startsWith("AA55") && message.endsWith("55AA")) {
            *//**
             * (6)	上报传感器数据指令：0xA6
             *//*
            String h = message.substring(6, 8);
            if (h.equals("A6")) {
                String deviceId = message.substring(12, 14);
                String sensorsType = message.substring(14, 16);
                String sensorsAddr = message.substring(16, 20);
                if (sensorsType.equals("01")) {
                    sensorsType = "湿度传感器";
                }
                if (sensorsType.equals("02")) {
                        sensorsType = "风速传感器";
                }
                if (sensorsType.equals("03")) {
                    sensorsType = "水盐传感器";
                }
                String sensorDataLen = message.substring(20,22);
                int len = Integer.valueOf(message.substring(20,22));
                String s = message.substring(22,22+2*len);
                float sensorsData = Float.parseFloat(s);
            }
        }*/
        String deviceId = message.substring(12, 14);
        String sensorsType = message.substring(14, 16);
        String sensorsAddr = message.substring(16, 20);
        if (sensorsType.equals("01")) {
            sensorsType = "湿度传感器";
        }
        if (sensorsType.equals("02")) {
            sensorsType = "风速传感器";
        }
        if (sensorsType.equals("03")) {
            sensorsType = "水盐传感器";
        }
        String sensorDataLen = message.substring(20,22);
        int len = Integer.valueOf(message.substring(20,22));
        String s = message.substring(22,22+2*len);
        float sensorsData = Float.parseFloat(s);
        influxDBContoller.insertOneToInflux(sensorsAddr,sensorsType,sensorsData);
    }
    @Override
    public void querySensorAdress(String message) throws Exception{
        //String message="AA550A0700010255AA";
        //StringBuilder sb = new StringBuilder();
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            String h = message.substring(6, 8);
            if (h.equals("A7")) {
                String s = message.substring(12, 14);
                String deviceId = message.substring(14, 16);
                if (s.equals("01"))
                    throw new Exception("确认帧错误，失败");
                //System.out.println("失败");
                if (s.equals("02"))
                    throw new Exception("CRC校验失败");
                //System.out.println(" CRC校验失败");
                if (s.equals("00"))
                {
                    AdminDtus adminDtus=new AdminDtus();
                    adminDtus.setDtuAddress(deviceId);
                    //System.out.println("数据终端设备地址为"+deviceId);
                    log.info("指令解析成功，数据终端设备地址为"+deviceId);
                }
            }
            if (h.equals("A8")) {
                String s = message.substring(12, 14);
                String deviceId = message.substring(14, 16);
                if (s.equals("01"))
                    throw new Exception("确认帧错误，失败");
                //System.out.println("失败");
                if (s.equals("02"))
                    throw new Exception("CRC校验失败");
                // System.out.println(" CRC校验失败");
                if (s.equals("00"))
                   log.info("指令解析成功，数据终端设备地址为"+deviceId);
            }
            if (h.equals("A9")) {
                String s = message.substring(12, 14);
                if (s.equals("01"))
                    throw new Exception("确认帧错误，失败");
                //System.out.println("失败");
                if (s.equals("02"))
                    throw new Exception("CRC校验失败");
                //System.out.println(" CRC校验失败");
                if (s.equals("00")) {
                    //数据终端设备地址
                    String s1 = message.substring(14, 16);
                   log.info("指令解析成功，数据终端设备地址为"+s1);
                    //电池电量
                    String s2 = message.substring(16, 20);
                    String sub = new BigInteger(s2, 16).toString(10);
                    AdminDtus adminDtus=new AdminDtus();
                    adminDtus.setDtuAddress(s1);
                   log.info("指令解析成功，电量为"+sub+"mA");
                    UpdateWrapper<AdminDtus> updateWrapper=new UpdateWrapper(adminDtus);
                    updateWrapper.set("elec_charge",sub);
                    iDtusAdminService.update(updateWrapper);
                }
            }
        }
    }
}