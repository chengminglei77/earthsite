package edu.xpu.cs.lovexian.app.appadmin.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


@Data
@TableName("kafaka_receive")
public class KafkaReceiveData {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 传感器数据,原始数据
     */
    @TableField("receive_data")
    private String receiveData;


    /**
     * 采集时间
     */
    @TableField("col_time")
    private Date colTime;

}
