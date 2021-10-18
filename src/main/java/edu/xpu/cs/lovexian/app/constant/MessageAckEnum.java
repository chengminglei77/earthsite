package edu.xpu.cs.lovexian.app.constant;

import lombok.Getter;

@Getter
public enum MessageAckEnum {

    SuccessMes("00","当前频率为："),

    FailMessage("01","失败"),

    FailCrcMessage("02","CRC失败"),

    DefalutMessage("-1","未知错误");

    MessageAckEnum(String code,String desc){
        this.ack = code;
        this.desc = desc;
    }

    public static MessageAckEnum findAck(String ack){
        for(MessageAckEnum rt: values()){
            if(rt.ack.equals(ack)){
                return rt;
            }
        }
        return DefalutMessage;
    }

    private String desc;
    private String ack;
}
