package edu.xpu.cs.lovexian.app.appadmin.Kafka;

import edu.xpu.cs.lovexian.app.appadmin.controller.InfluxDBContoller;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCollectData;
import edu.xpu.cs.lovexian.app.appadmin.mapper.CollectDataAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.impl.SensorsDataAdminServiceImpl;
import edu.xpu.cs.lovexian.common.utils.InstructionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class KafkaReceiver {
    public static Logger log = Logger.getLogger(KafkaReceiver.class);
    @Autowired
    SensorsDataAdminServiceImpl sensorsDataAdminService;
    @Autowired
    InfluxDBContoller influxDBContoller;
    CollectDataAdminMapper collectDataAdminMapper;
    @KafkaListener(topics = {"sensorsTopic"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String command=null;
            try{
                Object message = kafkaMessage.get();
                command = message.toString().substring(6,8);
                String Message=message.toString();
                System.out.println(Message);
                switch (command){
                    case "A1":
                        sensorsDataAdminService.setSensorAddrAndType(message.toString());break;
                        //TODO
                    case "A2":
                        sensorsDataAdminService.reportSensorAddrAndTypeAndNum(message.toString());break;
                        //TODO
                    case "A3":
                        sensorsDataAdminService.deleteSensor(message.toString());break;
                        //TODO
                    case "A4":
                        System.out.println("此处调用方法A4");break;
                       //TODO
                    case "A5":
                        System.out.println("此处调用方法A5");break;
                       //TODO
                    case "A6":
                        System.out.println(message);
                        sensorsDataAdminService.ReportSensorDataCommand(message.toString());
                        /*String[] sensorType = InstructionUtil.getSensorType(Message);
                        String sensorId = InstructionUtil.getDeviceId(Message);
                        AdminCollectData data = new AdminCollectData();
                        data.setSensorType(sensorType[0]);
                        data.setSensorId(sensorId);
                        collectDataAdminMapper.insert(data);*/
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
                        System.out.println("A8="+Message);
                        String deviceId = InstructionUtil.getDeviceId(Message);
                        String change = InstructionUtil.getChange(Message);
                        String batteryLevel = InstructionUtil.getBatteryLevel(Message);
                        influxDBContoller.insertTwoToInfluxDB(deviceId,change,batteryLevel);
                        break;
                    }
                    case "A9":
                    {   String ack = InstructionUtil.getAck(Message);
                        String deviceId = InstructionUtil.getDeviceId(Message);
                        String change = InstructionUtil.getChange(Message);
                        String batteryLevel = InstructionUtil.getBatteryLevel(Message);
                        if(ack.equals("01"))
                            log.error("确认帧错误，失败");
                        if (ack.equals("02"))
                            log.error("CRC校验失败");
                        if(ack.equals("00")) {
                            log.info("指令解析成功，数据终端设备地址为" + deviceId);
                            log.info("数据终端设备电量为：" + batteryLevel);
                            log.info("数据终端设备的充电状态为：" + change);
                        }    break;
                    }
                    default:
                        log.error("传入数据不合法");
                }
            }catch (Exception e){
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }
}