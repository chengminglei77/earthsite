package edu.xpu.cs.lovexian.phone.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author makejava
 * @since 2021-05-06 11:36:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sensor_data")
public class SensorData extends Model {
    private static final long serialVersionUID = -14929259540635289L;

    @TableField("id")
    private String id;

    @TableField("sensor_id")
    private String sensorId;

    @TableField("sensor_type")
    private String sensorType;

    @TableField("sensor_value")
    private String sensorValue;

    @TableField("sensor_param")
    private String sensorParam;

    @TableField("col_time")
    private Date colTime;

}