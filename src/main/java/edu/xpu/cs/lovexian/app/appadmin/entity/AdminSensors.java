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
 * @author xpu
 * @date 2020-09-01 22:02:59
 */
@Data
@TableName("sensors")
public class AdminSensors {

    /**
     * 传感器标识
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 传感器型号
     */
    @TableField("sensor_id")
    private String sensorId;

    /**
     * 传感器类别
     */
    @TableField("type_id")
    private String typeId;

    /**
     * 经度
     */
    @TableField("longitude")
    private Double longitude;

    /**
     * 纬度
     */
    @TableField("latitude")
    private Double latitude;

    /**
     * 位置信息
     */
    @TableField("desc_info")
    private String descInfo;

    /**
     * 采样频率
     */
    @TableField("sampling_frequency")
    private String samplingFrequency;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 部署时间
     */
    @TableField("created_at")
    private Date createdAt;

    /**
     * 最后更新时间
     */
    @TableField("updated_at")
    private Date updatedAt;

    /**
     * gis_info
     */
    @TableField("dis_info")
    private String disInfo;

    /**
     * 删除状态
     */
    @TableField("delete_state")
    private Integer deleteState;

    /**
     * 传感器的采样频率
     */
    @TableField("sensor_frequency")
    private String sensorFrequency;
    /*
    传感器的编号
     */
    @TableField("Sensor_Serial_Num")
    private String sensorSerialNum;
    /*
    传感器的地址
     */
    @TableField("Sensor_Address")
    private String sensorAddress;
}
