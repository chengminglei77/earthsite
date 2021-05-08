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
 * @since 2021-05-06 11:19:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("dtus")
public class Dtus extends Model {
    private static final long serialVersionUID = -27446804571828579L;

    @TableField("id")
    private String id;

    @TableField("dtu_id")
    private String dtuId;

    @TableField("dtu_name")
    private String dtuName;

    @TableField("longitude")
    private Double longitude;

    @TableField("latitude")
    private Double latitude;

    @TableField("desc_info")
    private String descInfo;

    @TableField("dtu_type")
    private String dtuType;

    @TableField("elc_volume")
    private String elcVolume;

    @TableField("elc_power")
    private String elcPower;

    @TableField("elc_status")
    private Integer elcStatus;

    @TableField("status")
    private Integer status;

    @TableField("signal_density")
    private String signalDensity;

    @TableField("bat_capacity")
    private String batCapacity;

    @TableField("elec_charge")
    private String elecCharge;

    @TableField("rate")
    private String rate;

    @TableField("frequency")
    private String frequency;

    @TableField("created_at")
    private Date createdAt;

    @TableField("updated_at")
    private Date updatedAt;

    @TableField("dis_info")
    private String disInfo;

    @TableField("del_state")
    private Integer delState;

}