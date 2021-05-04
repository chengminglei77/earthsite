package edu.xpu.cs.lovexian.app.appadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author czy
 * @create 2021-05-03-17:03
 */
@Data
@TableName("at_data")
public class AdminAtData {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     *  //①+++ ②AT ③ATI ④ATT 具体命令
     */
    @TableField("at")
    private String at;
    /**
     * 创建的时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 包含的信息
     */
    @TableField("data")
    private  String data;
}
