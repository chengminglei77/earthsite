package edu.xpu.cs.lovexian.app.appadmin.Kafka;

import edu.xpu.cs.lovexian.app.appadmin.controller.InfluxDBContoller;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCollectData;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminUnresovledData;
import edu.xpu.cs.lovexian.app.appadmin.entity.KafkaReceiveData;
import edu.xpu.cs.lovexian.app.appadmin.mapper.CollectDataAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.KafkaReceiveDataMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.UnresovledDataMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IDeviceStatisticsAdminService;
import edu.xpu.cs.lovexian.app.appadmin.service.impl.SensorsDataAdminServiceImpl;
import edu.xpu.cs.lovexian.common.utils.InstructionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Optional;

@Component
public class KafkaReceiver {
    public static Logger log = Logger.getLogger(KafkaReceiver.class);
    @Autowired
    SensorsDataAdminServiceImpl sensorsDataAdminService;
    @Autowired
    InfluxDBContoller influxDBContoller;
    @Autowired
    CollectDataAdminMapper collectDataAdminMapper;
    @Autowired
    UnresovledDataMapper unresovledDataMapper;
    @Autowired
    KafkaReceiveDataMapper kafkaReceiveDataMapper;
    @Autowired
    IDeviceStatisticsAdminService deviceStatisticsAdminService;
    @KafkaListener(topics = {"sensorsTopic"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String command=null;
            try{
                Object message = kafkaMessage.get();
                command = message.toString().substring(6,8);
                String Message=message.toString();
                //插入到mysql数据库表A6_data
                Date time2 = new java.sql.Date(new java.util.Date().getTime());
                KafkaReceiveData kafkaReceiveData = new KafkaReceiveData();
                kafkaReceiveData.setReceiveData((message.toString()));
                kafkaReceiveData.setColTime(time2);
                kafkaReceiveDataMapper.insert(kafkaReceiveData);
                switch (command){
                    case "A1":
                        sensorsDataAdminService.setSensorAddrAndType(message.toString());break;
                    case "A2":
                        sensorsDataAdminService.reportSensorAddrAndTypeAndNum(message.toString());break;
                    case "A3":
                        sensorsDataAdminService.deleteSensor(message.toString());break;
                    case "A4":
                        System.out.println("此处调用方法A4");break;
                    case "A5":
                        System.out.println("此处调用方法A5");break;
                    case "A6":
                        System.out.println("A6:"+message);
                        //插入到influxdb
                        sensorsDataAdminService.ReportSensorDataCommand(message.toString());
                        //插入到mysql数据库表collect_datas
                        Date time0 = new java.sql.Date(new java.util.Date().getTime());
                        String[] sensorType = InstructionUtil.getSensorType(Message);
                      /*  String sensorId = InstructionUtil.getDeviceId(Message);
                        AdminCollectData data = new AdminCollectData();
                        data.setSensorType(sensorType[0]);
                        data.setSensorId(sensorId);
                        data.setColTime(time0);
                        //data.setSensorValue(InstructionUtil.getSensorData(message.toString()));
                        collectDataAdminMapper.insert(data);*/
                        //插入到mysql数据库表A6_data
                        Date time1 = new java.sql.Date(new java.util.Date().getTime());
                        AdminUnresovledData adminUnresovledData = new AdminUnresovledData();
                        adminUnresovledData.setData(message.toString());
                        adminUnresovledData.setSensorType(sensorType[0]);
                        adminUnresovledData.setSensorData(InstructionUtil.getSensorData(message.toString()));
                        adminUnresovledData.setInstructionType(command);
                        adminUnresovledData.setColTime(time1);
                        unresovledDataMapper.insert(adminUnresovledData);
                        break;
                    //data.setSensorValue(sensorValue);
                    //collectDataAdminMapper.insert(data);
                    //influxDBContoller.insertOneToInflux("2020测试","风速传感器",25);
                    case "A7":
                    {
                        String ack = InstructionUtil.getAck(Message);
                        String deviceId = InstructionUtil.getDeviceId(Message);
                        if(ack.equals("01"))
                        log.error("确认帧错误，失败");
                        if (ack.equals("02"))
                        log.error("CRC校验失败");
                        if(ack.equals("00"))
                        log.info("指令解析成功，数据终端设备地址为"+deviceId);
                        break;
                    }
                    case "A8":
                    {
                        System.out.println("A8:"+message);
                        String deviceId = InstructionUtil.getDeviceId(Message);
                        String change = InstructionUtil.getChange(Message);
                        float batteryLevel = InstructionUtil.getBatteryLevel(Message);
                        influxDBContoller.insertTwoToInfluxDB(deviceId,change,batteryLevel);
                        //插入到MYSQL数据库A6_data
                        Date time = new java.sql.Date(new java.util.Date().getTime());
                        AdminUnresovledData adminUnresovledData1 = new AdminUnresovledData();
                        adminUnresovledData1.setData(message.toString());
                        adminUnresovledData1.setSensorType(deviceId);
                        adminUnresovledData1.setSensorData(String.valueOf(batteryLevel));
                        adminUnresovledData1.setInstructionType("A8");
                        System.out.println("deviceId为"+deviceId);
                        String settingId=InstructionUtil.getSettingId(deviceId);
                        adminUnresovledData1.setColTime(time);
                        adminUnresovledData1.setSettingId(settingId);
                        deviceStatisticsAdminService.insertDeviceStatistic(message.toString(),settingId);

                        unresovledDataMapper.insert(adminUnresovledData1);
                        break;
                    }
                    case "A9":
                    {   String ack = InstructionUtil.getAck(Message);
                        String deviceId = InstructionUtil.getDeviceId(Message);
                        String change = InstructionUtil.getChange(Message);
                        float batteryLevel = InstructionUtil.getBatteryLevel(Message);
                        if(ack.equals("01"))
                            log.error("确认帧错误，失败");
                        if (ack.equals("02"))
                            log.error("CRC校验失败");
                        if(ack.equals("00"))
                            log.info("指令解析成功，数据终端设备地址为"+deviceId);
                            log.info("数据终端设备电量为："+batteryLevel);
                            log.info("数据终端设备的充电状态为："+change);
                        break;
                    }
                    default:
                        System.out.println(message);
                        log.error("传入数据不合法"+message);
                }
            }catch (Exception e){
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }
}