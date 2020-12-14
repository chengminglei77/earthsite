package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.controller.InfluxDBContoller;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDecodeData;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtuSensor;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtus;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensorsData;
import edu.xpu.cs.lovexian.app.appadmin.mapper.CollectDataAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.DecodeDataMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.DtuSensorAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.SensorsDataAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IDtusAdminService;
import edu.xpu.cs.lovexian.app.appadmin.service.ISensorsDataAdminService;
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
    @Autowired
    private DtuSensorAdminMapper dtuSensorAdminMapper;
    @Autowired
    CollectDataAdminMapper collectDataAdminMapper;
    @Autowired
    DecodeDataMapper decodeDataMapper;
    @Override
    public void setSensorAddrAndType(String message){
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            String h = message.substring(6, 8);
            if (h.equals("A1")){
                String ACK = message.substring(12, 14);
                if (ACK.equals("01"))
                    log.info("成功");
                if (ACK.equals("02"))
                    log.info("CRC校验失败");
                if(ACK.equals("00")) {
                    AdminSensorsData data = new AdminSensorsData();
                    String Device_ID = message.substring(14, 16);
                    data.setDeviceId(Device_ID);
                    sensorsDataAdminMapper.insert(data);
                    log.info("成功");
                }
            }
        }
    }
    @Override
    public void reportSensorAddrAndTypeAndNum(String message){
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            String h = message.substring(6, 8);
            if (h.equals("A2")){
                String ACK = message.substring(12, 14);
                if (ACK.equals("01"))
                    log.info("失败");
                if (ACK.equals("02"))
                    log.info("CRC校验失败");
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
                    log.info("成功");
                }
            }
        }
    }
    @Override
    public void deleteSensor(String message){
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            String h = message.substring(6, 8);
            if (h.equals("A3")){
                String ACK = message.substring(12, 14);
                if (ACK.equals("01"))
                    log.info("失败");
                if (ACK.equals("02"))
                    log.info("CRC校验失败");
                if(ACK.equals("00")) {
                    AdminSensorsData data = new AdminSensorsData();
                    String Device_ID = message.substring(14, 16);
                    data.setDeviceId(Device_ID);
                    sensorsDataAdminMapper.insert(data);
                    log.info("成功");
                }
            }
        }
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
                    //System.out.println("成功");
                    log.info("成功");
                }else {
                    if (CRC.equals("01")){
                        //System.out.println("失败");
                        log.info("失败");
                    }else {
                        //System.out.println("CRC校验失败");
                        log.info("CRC校验失败");
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
                    //AdminSensorsData data = new AdminSensorsData();
                    AdminDtuSensor data = new AdminDtuSensor();
                    int time;
                    String deviceId = message.substring(14,16);
                    String str = message.substring(16,20);
                    String sub = new BigInteger(str, 16).toString(10);
                    time = Integer.parseInt(sub);
                    data.setTime(time);
                    dtuSensorAdminMapper.insert(data);//将传感其上报时间存到dtu_sensor表中
                }else {
                    if (ACK.equals("01")){
                        //System.out.println( "失败");
                        log.info("失败");
                    }else {
                        if (ACK.equals("02")){
                            //System.out.println("校验失败");
                            log.info("CRC校验失败");
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
        String s0 = message.substring(20,22);
        String len = new BigInteger(s0, 16).toString(10);
        int length = Integer.valueOf(len);
        String s = message.substring(22,22+2*length);
        String sensorsData = s;
        //float sensorsData = Float.parseFloat(s);
        influxDBContoller.insertOneToInflux(sensorsAddr,sensorsType,sensorsData);

        AdminDecodeData adminDecodeData = new AdminDecodeData();
        //1号传感器3s时的平均风速和风向
        /**
         * 平均风速
         */
        String sensor_data_speed_3s_no1 = message.substring(22,26);
        //System.out.println(sensor_data_speed_3s_no1);
        String speed_3s_no1 = new BigInteger(sensor_data_speed_3s_no1,16).toString(10);
        int windSpeed_3s_no1 = Integer.parseInt(speed_3s_no1);
        adminDecodeData.setSpeed3sNo1(windSpeed_3s_no1);
        //System.out.println("3s_风速_no1："+windSpeed_3s_no1);
        /**
         * 风向
         */
        String sensor_data_direction_3s_no1 = message.substring(26,30);
        //System.out.println(sensor_data_direction_3s_no1);
        String direction_3s_no1 = new BigInteger(sensor_data_direction_3s_no1,16).toString(10);
        int windDirection_3s_no1 = Integer.parseInt(direction_3s_no1)+92;
        adminDecodeData.setDirection3sNo1(windDirection_3s_no1);
        //System.out.println("3s_风向_no1："+windDirection_3s_no1);


        //1号传感器2min时的平均风速和风向
        /**
         * 平均风速
         */
        String sensor_data_speed_2min_no1 = message.substring(30,34);
        //System.out.println(sensor_data_speed_2min_no1);
        String speed_2min_no1 = new BigInteger(sensor_data_speed_2min_no1,16).toString(10);
        int windSpeed_2min_no1 = Integer.parseInt(speed_2min_no1);
        adminDecodeData.setSpeed2minNo1(windSpeed_2min_no1);
        //System.out.println("2min_风速_no1："+windSpeed_2min_no1);
        /**
         * 风向
         */
        String sensor_data_direction_2min_no1 = message.substring(34,38);
        //System.out.println(sensor_data_direction_2min_no1);
        String direction_2min_no1 = new BigInteger(sensor_data_direction_2min_no1,16).toString(10);
        int windDirection_2min_no1 = Integer.parseInt(direction_2min_no1)+92;
        adminDecodeData.setDirection2minNo1(windDirection_2min_no1);
        //System.out.println("2min_风向_no1："+windDirection_2min_no1);


        //1号传感器10min时的平均风速和风向
        /**
         * 平均风速
         */
        String sensor_data_speed_10min_no1 = message.substring(42,46);
        //System.out.println(sensor_data_speed_10min_no1);
        String speed_10min_no1 = new BigInteger(sensor_data_speed_10min_no1,16).toString(10);
        int windSpeed_10min_no1 = Integer.parseInt(speed_10min_no1);
        adminDecodeData.setSpeed10minNo1(windSpeed_10min_no1);
        //System.out.println("10min_风速_no1："+windSpeed_10min_no1);
        /**
         * 风向
         */
        String sensor_data_direction_10min_no1 = message.substring(46,50);
        //System.out.println(sensor_data_direction_10min_no1);
        String direction_10min_no1 = new BigInteger(sensor_data_direction_10min_no1,16).toString(10);
        int windDirection_10min_no1 = Integer.parseInt(direction_10min_no1)+92;
        adminDecodeData.setDirection10minNo1(windDirection_10min_no1);
        //System.out.println("10min_风向_no1："+windDirection_10min_no1);



        //2号传感器3s时的平均风速和风向
        /**
         * 平均风速
         */
        String sensor_data_speed_3s_no2 = message.substring(50,54);
        //System.out.println(sensor_data_speed_3s_no2);
        String speed_3s_no2 = new BigInteger(sensor_data_speed_3s_no2,16).toString(10);
        int windSpeed_3s_no2 = Integer.parseInt(speed_3s_no2);
        adminDecodeData.setSpeed3sNo2(windSpeed_3s_no2);
        //System.out.println("3s_风速_no2："+windSpeed_3s_no2);
        /**
         * 风向
         */
        String sensor_data_direction_3s_no2 = message.substring(54,58);
        //System.out.println(sensor_data_direction_3s_no2);
        String direction_3s_no2 = new BigInteger(sensor_data_direction_3s_no2,16).toString(10);
        int windDirection_3s_no2 = Integer.parseInt(direction_3s_no2)-22;
        adminDecodeData.setDirection3sNo2(windDirection_3s_no2);
        //System.out.println("3s_风向_no2："+windDirection_3s_no2);


        //2号传感器2min时的平均风速和风向
        /**
         * 平均风速
         */
        String sensor_data_speed_2min_no2 = message.substring(58,62);
        //System.out.println(sensor_data_speed_2min_no2);
        String speed_2min_no2 = new BigInteger(sensor_data_speed_2min_no2,16).toString(10);
        int windSpeed_2min_no2 = Integer.parseInt(speed_2min_no2);
        adminDecodeData.setSpeed2minNo2(windSpeed_2min_no2);
        //System.out.println("2min_风速_no2："+windSpeed_2min_no2);
        /**
         * 风向
         */
        String sensor_data_direction_2min_no2 = message.substring(62,66);
        //System.out.println(sensor_data_direction_2min_no2);
        String direction_2min_no2 = new BigInteger(sensor_data_direction_2min_no2,16).toString(10);
        int windDirection_2min_no2 = Integer.parseInt(direction_2min_no2)-22;
        adminDecodeData.setDirection2minNo2(windDirection_2min_no2);
        //System.out.println("2min_风向_no2："+windDirection_2min_no2);


        //2号传感器10min时的平均风速和风向
        /**
         * 平均风速
         */
        String sensor_data_speed_10min_no2 = message.substring(66,70);
        //System.out.println(sensor_data_speed_10min_no2);
        String speed_10min_no2 = new BigInteger(sensor_data_speed_10min_no2,16).toString(10);
        int windSpeed_10min_no2 = Integer.parseInt(speed_10min_no2);
        adminDecodeData.setSpeed10minNo2(windSpeed_10min_no2);
        //System.out.println("10min_风速_no2："+windSpeed_10min_no2);
        /**
         * 风向
         */
        String sensor_data_direction_10min_no2 = message.substring(70,74);
        //System.out.println(sensor_data_direction_10min_no2);
        String direction_10min_no2 = new BigInteger(sensor_data_direction_10min_no2,16).toString(10);
        int windDirection_10min_no2 = Integer.parseInt(direction_10min_no2)-22;
        adminDecodeData.setDirection10minNo2(windDirection_10min_no2);
        //System.out.println("10min_风向_no2："+windDirection_10min_no2);

        //温度的正负
        String negative_positive = message.substring(74,76);//sensor_data_temperature
        String sensor_data_temperature = message.substring(76,78);
        String s1 = new BigInteger(sensor_data_temperature,16).toString(10);
        if (negative_positive.equals("00")){
            String symbol = "-";
            String centigrade = "℃";
            String temperature = symbol+s1+centigrade;
            //int temperature = Integer.parseInt(s1);
            adminDecodeData.setTemperature(temperature);
        }else{
            adminDecodeData.setTemperature(s1);
        }
        adminDecodeData.setSensor_Type(sensorsType);
        adminDecodeData.setInstructionType("A6");
        java.sql.Date colTime = new java.sql.Date(new java.util.Date().getTime());
        adminDecodeData.setColTime(colTime);
        decodeDataMapper.insert(adminDecodeData);
        //System.out.println(temperature);
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