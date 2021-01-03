package edu.xpu.cs.lovexian.app.appadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 律师信息表 Entity
 *
 * @author xpu
 * @date 2019-12-25 15:25:48
 */
@Data
@TableName("lawer_info")
public class AdminLawerInfo {

    /**
     *
     */

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 律师姓名
     */
    @TableField("lawer_name")
    private String lawerName;

    /**
     * 律师头像
     */
    @TableField("lawer_head_photo")
    private String lawerHeadPhoto;

    /**
     *
     */
    @TableField("update_time")
    private Date updateTime;

    @TableField("create_time")
    private Date createTime;

    /**
     * 律师工作机构
     */
    @TableField("lawer_organization")
    private String lawerOrganization;

    /**
     * 定位经度
     */
    @TableField("location_longitude")
    private Double locationLongitude;

    /**
     * 定位纬度
     */
    @TableField("location_latitude")
    private Double locationLatitude;

    /**
     * 定位位置名
     */
    @TableField("location_name")
    private String locationName;

    /**
     * 工作年限
     */
    @TableField("work_time")
    private Date workTime;

    /**
     * 点赞数
     */
    @TableField("like_num")
    private Integer likeNum;

    /**
     * 律师的星级
     */
    @TableField("star")
    private Integer star;

    /**
     * 律师擅长领域
     */
    @TableField("skill_field")
    private String skillField;

    /**
     * 律师简介
     */
    @TableField("lawer_abstract")
    private String lawerAbstract;

    /**
     * 语言类型
     */
    @TableField("language_type")
    private Integer languageType;

    /**
     *
     */
    @TableField("static_page")
    private String staticPage;

    /**
     * 律师手机号
     */
    @TableField("tel_number")
    private String telNumber;

    /**
     * 创建者name
     */
    @TableField("creator_name")
    private String creatorName;

    /**
     * 更新者name
     */
    @TableField("updater_name")
    private String updaterName;

    /**
     *
     */
    @TableField("is_show")
    private Integer isShow;

    @TableField("is_top")
    private Integer isTop;

    /**
     *
     */
    @TableField("check_state")
    private Integer checkState;

    /**
     * 删除状态
     */
    @TableField("del_state")
    private Integer delState;

    private transient String createTimeFrom;

    private transient String createTimeTo;
}
