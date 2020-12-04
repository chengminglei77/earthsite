package edu.xpu.cs.lovexian.app.appadmin.Kafka;

import edu.xpu.cs.lovexian.app.appadmin.controller.InfluxDBContoller;
import edu.xpu.cs.lovexian.app.appadmin.service.impl.SensorsDataAdminServiceImpl;
import edu.xpu.cs.lovexian.common.utils.InstructionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Optional;
@Slf4j
@Component
public class KafkaReceiver {
    @Autowired
    SensorsDataAdminServiceImpl sensorsDataAdminService;
    @Autowired
    InfluxDBContoller influxDBContoller;
    @KafkaListener(topics = {"sensorsTopic"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String command=null;
            try{
                Object message = kafkaMessage.get();
                command = message.toString().substring(6,8);
                String Message=message.toString();
                switch (command){
                    case "A1":
                        System.out.println("此处调用方法A1");break;
                        //TODO
                    case "A2":
                        System.out.println("此处调用方法A2");break;
                        //TODO
                    case "A3":
                        System.out.println("此处调用方法A3");break;
                        //TODO
                    case "A4":
                        System.out.println("此处调用方法A4");break;
                       //TODO
                    case "A5":
                        System.out.println("此处调用方法A5");break;
                       //TODO
                    case "A6":
                        sensorsDataAdminService.ReportSensorDataCommand(message.toString());
                        //influxDBContoller.insertOneToInflux("2020测试","风速传感器",25);
                        //TODO
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
                    }
                    case "A8":
                    {
                        String deviceId = InstructionUtil.getDeviceId(Message);
                        String change = InstructionUtil.getChange(Message);
                        String batteryLevel = InstructionUtil.getBatteryLevel(Message);
                        influxDBContoller.insertTwoToInfluxDB(deviceId,change,batteryLevel);
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
                        if(ack.equals("00"))
                            log.info("指令解析成功，数据终端设备地址为"+deviceId);
                            log.info("数据终端设备电量为："+batteryLevel);
                            log.info("数据终端设备的充电状态为："+change);
                    }
                    default:
                        System.out.println("传入数据不合法");
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("异常为:"+e.getMessage());
            }
        }
    }
}