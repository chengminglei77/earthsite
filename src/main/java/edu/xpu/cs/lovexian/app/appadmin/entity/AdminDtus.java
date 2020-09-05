package edu.xpu.cs.lovexian.app.appadmin.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  Entity
 *
 * @author xpu
 * @date 2020-09-01 21:29:32
 */
@Data
@TableName("dtus")
public class AdminDtus {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * dtu表示
     */
    @TableField("dtu_id")
    private String dtuId;

    /**
     * dtu名
     */
    @TableField("dtu_name")
    private String dtuName;
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
     * dtu类型
     */
    @TableField("dtu_type")
    private String dtuType;

    /**
     * 电池容量
     */
    @TableField("elc_volume")
    private String elcVolume;

    /**
     * 太阳能电池板功率
     */
    @TableField("elc_power")
    private String elcPower;

    /**
     * 充放电状态
     */
    @TableField("elc_status")
    private Integer elcStatus;

    /**
     * dtu状态
     * 0:未删除状态
     * 0以外的数:输出状态,不能显示在前端
     */
    @TableField("status")
    private Integer status;


    /**
     * 信号强度
     */
    @TableField("signal_density")
    private String signalDensity;

    /**
     * 当前电量
     */
    @TableField("elec_charge")
    private String elecCharge;

    /**
     * 速率
     */
    @TableField("rate")
    private String rate;

    /**
     * 频点
     */
    @TableField("frequency")
    private String frequency;

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
     * dis信息
     */
    @TableField("dis_info")
    private String disInfo;

}
