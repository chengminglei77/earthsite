package edu.xpu.cs.lovexian.app.appadmin.entity;


import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

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


    @TableField(exist = false)
    private String typeId;
    @TableField(exist = false)
    private String dtuName;

}