package edu.xpu.cs.lovexian.app.appadmin.Kafka;

import edu.xpu.cs.lovexian.app.appadmin.controller.InfluxDBContoller;
import edu.xpu.cs.lovexian.app.appadmin.entity.KafkaReceiveData;
import edu.xpu.cs.lovexian.app.appadmin.mapper.CollectDataAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.KafkaReceiveDataMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.UnresovledDataMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IDeviceStatisticsAdminService;
import edu.xpu.cs.lovexian.app.appadmin.service.impl.SensorsDataAdminServiceImpl;
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
    @Autowired
    PerformInstrution performInstrution;
    private  static  Boolean state=false;

    @KafkaListener(topics = {"sensorsTopic"})
    public void listen(ConsumerRecord<?, ?> record) {
      
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String command=null;
            try {
                Object messageReceive = kafkaMessage.get();
                String message2String = messageReceive.toString();
                if (messageReceive.toString().startsWith("0D0A") && messageReceive.toString().startsWith("0D0A")) {
                    performInstrution.performCommand(messageReceive.toString());
                }
            else{        String str3 = message2String.substring(0, message2String.lastIndexOf("55AA0D0A"));
                String str1[] = str3.split("55AA0D0A");
                for (String temp : str1) {
                    String message = temp + "55AA0D0A";

                System.out.println("message为" + message.toString());
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
                                if (Message.substring(8, 12) != "0002") {
                                    this.performInstrution.performA6(Message);
                                }
                                break;
                            case "A7": {
                                this.performInstrution.performA7(Message);
                                break;
                            }
                            case "A8": {
                                System.out.println("执行A8");
                                this.performInstrution.performA8(Message);
                                break;
                            }
                            case "A9": {
                                this.performInstrution.performA9(Message);
                                break;
                            }
                            default: {
                                System.out.println(message);

                            }

//                        log.error("传入数据不合法" + message);
                        }
                    }
                }
            }
            }catch (Exception e){
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }
}