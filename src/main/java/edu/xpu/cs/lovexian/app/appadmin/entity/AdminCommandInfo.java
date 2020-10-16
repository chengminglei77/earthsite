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
@TableName("command_history")
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
    private Integer cmd_status;

    /**
     * 发送时间
     */
    @TableField("send_time")
    private Date send_Time;
    /**
     * 接收时间
     */
    @TableField("receive_time")
    private Date receive_Time;

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

}
