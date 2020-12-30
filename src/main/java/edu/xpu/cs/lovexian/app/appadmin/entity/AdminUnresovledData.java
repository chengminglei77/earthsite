package edu.xpu.cs.lovexian.app.appadmin.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 *  Entity
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
     * 未解码的数据，远水数据
     */
    @TableField("data")
    private String data;
    /**
     * 设备ID
     */
    @TableField("setting_id")
    private  String settingId;
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

}
