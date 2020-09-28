package edu.xpu.cs.lovexian.app.appadmin.entity;


import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  Entity
 *
 * @author xpu
 * @date 2020-09-27 19:33:26
 */
@Data
@TableName("sensor_type")
public class AdminSensorType {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 传感器名
     */
    @TableField("sensor_name")
    private String sensorName;

    /**
     * 传感器类别
     */
    @TableField("sensor_type")
    private String sensorType;

    /**
     * 传感器型号
     */
    @TableField("sensor_model")
    private String sensorModel;

    /**
     * 生产厂家
     */
    @TableField("sensor_producer")
    private String sensorProducer;

    /**
     * 传感器描述
     */
    @TableField("sensor_desc")
    private String sensorDesc;

    /**
     * 传感器的采样频率
     */
    @TableField("sensor_frequency")
    private String sensorFrequency;

}
