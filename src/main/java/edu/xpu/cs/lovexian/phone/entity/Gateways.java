package edu.xpu.cs.lovexian.phone.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author makejava
 * @since 2021-05-06 11:37:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("gateways")
public class Gateways extends Model {
    private static final long serialVersionUID = 357412610145136684L;

    @TableField("id")
    private String id;

    @TableField("gate_id")
    private String gateId;

    @TableField("longitude")
    private Double longitude;

    @TableField("latitude")
    private Double latitude;

    @TableField("desc_info")
    private String descInfo;

    @TableField("status")
    private Integer status;

    @TableField("delete_state")
    private Long deleteState;

    @TableField("bat_capacity")
    private String batCapacity;

    @TableField("elec_charge")
    private String elecCharge;

    @TableField("server_ip")
    private String serverIp;

    @TableField("server_port")
    private String serverPort;

    @TableField("created_at")
    private Date createdAt;

    @TableField("updated_at")
    private Date updatedAt;

    @TableField("dis_info")
    private String disInfo;

    @TableField("elc_status")
    private Integer elcStatus;

}