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
    private Integer cmdStatus;

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
     * 计数
     */
    @TableField("count")
    private Integer count;

    /**
     * 描述信息
     */
    @TableField("description")
    private String description;

    private transient String createTimeFrom;

    private transient String createTimeTo;

    /*
    * 删除状态
    * */
    @TableField("delState")
    private Integer delState;
}
