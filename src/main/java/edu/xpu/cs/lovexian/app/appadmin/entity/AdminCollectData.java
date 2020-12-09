package edu.xpu.cs.lovexian.app.appadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 *  Entity
 *
 * @author xpu
 * @date 2020-12-07 17:00:00
 */
@Data
@TableName("collect_datas")
public class AdminCollectData {
    /**
     * 每条数据的id
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 传感器标识
     */
    @TableId(value = "sensor_id", type = IdType.ID_WORKER_STR)
    private String sensorId;

    /**
     * 传感器类型
     */
    @TableId(value = "sensor_type", type = IdType.ID_WORKER_STR)
    private String sensorType;

    /**
     * 传感器数据数据
     */
    @TableId(value = "sensor_value", type = IdType.ID_WORKER_STR)
    private double sensorValue;

    /**
     *收集时间
     */
    @TableId(value = "col_time", type = IdType.ID_WORKER_STR)
    private String colTime;
}
