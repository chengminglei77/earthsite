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
 * @author zhangAnJie
 * @date 2020-12-15 17:01:18
 */
@Data
@TableName("sensor_data")
public class AdminDecodeData {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 传感器类型
     */
    @TableField("sensor_type")
    private String sensor_Type;

    /**
     * 1号传感器3s时的风速
     */
    @TableField("speed_3s_no1")
    private double speed3sNo1;

    /**
     * 1号传感器3s时的风向
     */
    @TableField("direction_3s_no1")
    private Integer direction3sNo1;

    /**
     * 1号传感器2min时的风速
     */
    @TableField("speed_2min_no1")
    private double speed2minNo1;

    /**
     * 1号传感器2min时的风向
     */
    @TableField("direction_2min_no1")
    private Integer direction2minNo1;

    /**
     * 1号传感器10min时的风速
     */
    @TableField("speed_10min_no1")
    private double speed10minNo1;

    /**
     * 1号传感器10min时的风向
     */
    @TableField("direction_10min_no1")
    private Integer direction10minNo1;

    /**
     * 2号传感器3s时的风速
     */
    @TableField("speed_3s_no2")
    private double speed3sNo2;

    /**
     * 2号传感器3s时的风向
     */
    @TableField("direction_3s_no2")
    private Integer direction3sNo2;

    /**
     * 2号传感器2min时的风向
     */
    @TableField("speed_2min_no2")
    private double speed2minNo2;

    /**
     * 2号传感器2min时的风向
     */
    @TableField("direction_2min_no2")
    private Integer direction2minNo2;

    /**
     * 2号传感器10min时的风速
     */
    @TableField("speed_10min_no2")
    private double speed10minNo2;

    /**
     * 2号传感器10min时的风向
     */
    @TableField("direction_10min_no2")
    private Integer direction10minNo2;

    /**
     * 2号传感器10min时的风向
     */
    @TableField("temperature")
    private String temperature;

    /**
     * 传感器类型
     */
    @TableField("instruction_type")
    private String instructionType;

    /**
     * 采集时间
     */
    @TableField("col_time")
    private Date colTime;

}
