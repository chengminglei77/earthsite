package edu.xpu.cs.lovexian.app.appadmin.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Entity
 *
 * @author zhanganajie
 * @date 2020-09-01 17:01:18
 */
@Data
@TableName("A6_data")
public class AdminUnresovledData {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 未解码的数据，原始数据
     */
    @TableField("data")
    private String data;

    /**
     * 传感器类型
     */
    @TableField("sensor_type")
    private String sensorType;

    /**
     * 传感器数据
     */
    @TableField("sensor_data")
    private String sensorData;

    /**
     * 指令类型
     */
    @TableField("instruction_type")
    private String instructionType;

    /**
     * 采集时间
     */
    @TableField("col_time")
    private Date colTime;

    /**
     * 设备编号
     */
    @TableField("setting_id")
    private String settingID;

    /**
     * ack状态
     */
    @TableField("ack_state")
    private String ackState;

    /**
     * 帧序号
     */
    @TableField("frame_num")
    private String frameNum;

}
