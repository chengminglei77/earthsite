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
@TableName("cmd_history")
public class AdminCommandInfo {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 命令信息
     */
    @TableField("command")
    private String command;

    /**
     * 处理状态
     */
    @TableField("cmd_status")
    private String status;

    /**
     * 发送时间
     */
    @TableField("send_time")
    private Date sendTime;

    /**
     * 接收时间
     */
    @TableField("receive_time")
    private Date receiveTime;


    /**
     * 描述信息
     */
    @TableField("description")
    private String description;

    /**
     * 设备ID
     */
    @TableField("device_id")
    private String deviceID;

    /**
     * 解析内容
     */
    @TableField("content")
    private String content;

    /**
     * TYPE
     */
    @TableField("type")
    private String type;

    /**
     * 帧序号
     */
    @TableField("frame_num")
    private String frameNum;

    private transient String sendTimeFrom;

    private transient String sendTimeTo;


}
