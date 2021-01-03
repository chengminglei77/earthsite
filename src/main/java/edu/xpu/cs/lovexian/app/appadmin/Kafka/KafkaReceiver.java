package edu.xpu.cs.lovexian.app.appadmin.Kafka;

import edu.xpu.cs.lovexian.app.appadmin.controller.InfluxDBContoller;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCollectData;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminUnresovledData;
import edu.xpu.cs.lovexian.app.appadmin.entity.KafkaReceiveData;
import edu.xpu.cs.lovexian.app.appadmin.mapper.CollectDataAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.GatewayDtuAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.KafkaReceiveDataMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.UnresovledDataMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IDeviceStatisticsAdminService;
import edu.xpu.cs.lovexian.app.appadmin.service.impl.SensorsDataAdminServiceImpl;
import edu.xpu.cs.lovexian.common.utils.InstructionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
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
    @Autowired
    PerformInstrution performInstrution;
    @Autowired
    GatewayDtuAdminMapper gatewayDtuAdminMapper;
    @KafkaListener(topics = {"sensorsTopic"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String command=null;
            try {
                Object message = kafkaMessage.get();
                command = message.toString().substring(6, 8);
                String Message = message.toString();
                boolean ifTrueMes = Message.startsWith("A");
                if (ifTrueMes == true) {
                //插入到mysql数据库表A6_data
                Date time2 = new java.sql.Date(new java.util.Date().getTime());
                KafkaReceiveData kafkaReceiveData = new KafkaReceiveData();
                kafkaReceiveData.setReceiveData((message.toString()));
                kafkaReceiveData.setColTime(time2);
                kafkaReceiveDataMapper.insert(kafkaReceiveData);
                switch (command) {
                    case "A1":
                        sensorsDataAdminService.setSensorAddrAndType(message.toString());
                        break;
                    case "A2":
                        sensorsDataAdminService.reportSensorAddrAndTypeAndNum(message.toString());
                        break;
                    case "A3":
                        sensorsDataAdminService.deleteSensor(message.toString());
                        break;
                    case "A4":
                        System.out.println("此处调用方法A4");
                        break;
                    case "A5":
                        System.out.println("此处调用方法A5");
                        break;
                    case "A6":
                        System.out.println("A6:" + message);
                        performInstrution.performA6(Message);
                        //插入到A6_data
                        Date time = new java.sql.Date(new java.util.Date().getTime());
                        String[] sensorType = InstructionUtil.getSensorType(Message);
                        //插入到mysql数据库表A6_data
                        AdminUnresovledData adminUnresovledData = new AdminUnresovledData();
                        adminUnresovledData.setData(message.toString());
                        adminUnresovledData.setSensorType(sensorType[0]);
                        adminUnresovledData.setSensorData(InstructionUtil.getSensorData(Message));
                        adminUnresovledData.setInstructionType(command);
                        adminUnresovledData.setFrameNum(InstructionUtil.getFrameNum(Message));
                        adminUnresovledData.setColTime(time);
                        unresovledDataMapper.insert(adminUnresovledData);

                        break;
                    case "A7": {
                        performInstrution.performA7(Message);
                        break;
                    }
                    case "A8": {
                        performInstrution.performA8(Message);
                        break;
                    }
                    case "A9": {
                      performInstrution.performA9(Message);
                      break;
                    }
                    default:
                        System.out.println(message);
                        log.error("传入数据不合法" + message);
                }

            }
            }catch (Exception e){
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }
}