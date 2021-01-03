package edu.xpu.cs.lovexian.app.appadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author czy
 * @create 2020-10-25-18:04
 */
@Data
@TableName("eq_statistics")
public class AdminDeviceStatistics {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 工作时间
     */
    @TableField("eq_duration")
    private int eqDuration;

    /**
     * 信息包总量大小，每个信息包大小是固定的
     */
    @TableField("packet_size")
    private int packetSize;

    /**
     * 信息总量
     */
    @TableField("info_total")
    private int infoTotal;

    /**
     * 设备型号
     */
    @TableField("setting_id")
    private String settingId;

    /**
     * 设备类型，传感器管理还是dtu管理...
     */
    @TableField("type")
    private String type;

    @TableField("updated_at")
    private Date updatedAt;
}
