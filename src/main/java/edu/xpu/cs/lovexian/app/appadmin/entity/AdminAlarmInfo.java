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
 * @date 2020-09-21 19:42:21
 */
@Data
@TableName("alarm_info")
public class AdminAlarmInfo {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;


    //设备id：唯一标识
    @TableField("device_id")
    private String deviceId;

    /**
     * 警报信息
     */
    @TableField("alarm_info")
    private String alarmInfo;

    /**
     * 处理状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 警报产生时间
     */
    @TableField("alarm_time")
    private Date alarmTime;

    /**
     * 处理人
     */
    @TableField("deal_admin")
    private String dealAdmin;

    /**
     * 处理时间
     */
    @TableField("deal_time")
    private Date dealTime;

    /**
     * 警报原因
     */
    @TableField("alarm_reason")
    private String alarmReason;

    /**
     * 删除状态
     */
    @TableField("delete_state")
    private Integer deleteState;


    @TableField(exist = false)
    private String sensorId;

    @TableField(exist = false)
    private String dtuId;

    private transient String createTimeFrom;

    private transient String createTimeTo;
}
