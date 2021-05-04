package edu.xpu.cs.lovexian.app.appadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author czy
 * @create 2021-04-30-21:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("at_command")
//与前端互联的网关配置类
public class AdminGatewayConfig {
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
     * 命令所要执行的内容
     */
    @TableField("message")
    private String message;
    /**
     *具体命令对应的ASCII码
     */
    @TableField("ascii")
    private String ascii;

}
