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
 * @date 2020-09-01 17:01:18
 */
@Data
@TableName("gateways")
public class AdminGateways {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 网关标识
     */
    @TableField("gate_id")
    private String gateId;

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
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 当前电量
     */
    @TableField("elec_charge")
    private String elecCharge;

    /**
     * 服务器地址
     */
    @TableField("server_ip")
    private String serverIp;

    /**
     * 服务器端口
     */
    @TableField("server_port")
    private String serverPort;

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
     * gis信息
     */
    @TableField("dis_info")
    private String disInfo;

}
