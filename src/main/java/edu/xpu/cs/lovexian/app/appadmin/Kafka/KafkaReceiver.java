package edu.xpu.cs.lovexian.app.appadmin.Kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class KafkaReceiver {
    @KafkaListener(topics = {"test001_topic"})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String command=null;
            try{
                Object message = kafkaMessage.get();
                command = message.toString().substring(6,8);
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
                        System.out.println("此处调用方法A6");break;
                       //TODO
                    case "A7":
                        System.out.println("此处调用方法A7");break;
                       //TODO
                    case "A8":
                        System.out.println("此处调用方法A8");break;
                       //TODO
                    case "A9":
                        System.out.println("此处调用方法A9");break;
                        //TODO
                    default:
                        System.out.println("传入数据不合法");
                }
            }catch (Exception e){
                System.out.println("传入数据可能出现异常");
                e.printStackTrace();
            }
        }
    }
}