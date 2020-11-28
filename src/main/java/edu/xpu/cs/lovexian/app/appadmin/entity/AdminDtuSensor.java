package edu.xpu.cs.lovexian.app.appadmin.entity;


import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 *  Entity
 *
 * @author xpu
 * @date 2020-09-21 16:34:55
 */
@Data
@TableName("dtu_sensor")
public class AdminDtuSensor implements Serializable {

    /**
     * 每条数据的id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * dtu标识
     */
    @TableId(value = "dtu_id", type = IdType.ID_WORKER_STR)
    private String dtuId;

    /**
     * 传感器标识
     */
    @TableId(value = "sensor_id", type = IdType.ID_WORKER_STR)
    private String sensorId;

    /*
    传感器个数
     */
    @TableField("Sensor_Number")
    private  int sensorNumber;

    /*
    传感器上传的时间
     */
    @TableField("time")
    private int time;

    /*
    传感器数据长度
     */
    @TableField("Sensor_Data_Len")
    private int sensorDataLen;
    /*
    传感器数据
     */
    @TableField("Sensor_Data")
    private String SensorData;

    @TableField(exist = false)
    private String typeId;
    @TableField(exist = false)
    private String dtuName;
    @TableField(exist = false)
    private Integer status;
    @TableField(exist = false)
    private Date createdAt;

}
