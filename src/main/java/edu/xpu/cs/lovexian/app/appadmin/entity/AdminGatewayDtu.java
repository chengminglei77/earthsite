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
 * @date 2020-09-01 21:51:18
 */
@Data
@TableName("gateway_dtu")
public class AdminGatewayDtu {
    /**
     * 每条数据的id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 网关标识
     */
    @TableId(value = "gateway_id", type = IdType.ID_WORKER_STR)
    private String gatewayId;

    /**
     * dtu标识
     */
    @TableId(value = "dtu_id", type = IdType.ID_WORKER_STR)
    private String dtuId;


    @TableField(exist = false)
    private String dtuName;

    @TableField(exist = false)
    private Double longitude;

    @TableField(exist = false)
    private Double latitude;

    @TableField(exist = false)
    private String descInfo;

    @TableField(exist = false)
    private String dtuType;

    @TableField(exist = false)
    private Integer status;

    @TableField(exist = false)
    private Date createdAt;

    @TableField(exist = false)
    private Date updatedAt;

}
