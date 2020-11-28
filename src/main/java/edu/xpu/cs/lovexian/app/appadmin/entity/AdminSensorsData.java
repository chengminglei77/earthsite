package edu.xpu.cs.lovexian.app.appadmin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author czy
 * @create 2020-11-27-9:56
 */
@Data
@TableName("sensor_data")
public class AdminSensorsData {
    /*
    传感器标识
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    /*
    数据终端设备地址
    */
    @TableField("Device_ID")
    private String deviceId;

    /*
   传感器编号
   */
    @TableField("Sensor_Serial_Num")
    private String sensorSerialNum;

    /*
    传感器类型
    */
    @TableField("Sensor_Type")
    private String sensorType;

    /*
   传感器地址
   */
    @TableField("Sensor_Addr")
    private String sensorAddr;

    /*
   当前上报数据的传感器个数
   */
    @TableField("Sensor_Num")
    private int sensorNum;

    /*
   传感器上报数据时间
   */
    @TableField("Time")
    private Date time;

    /*
   传感器数据长度
   */
    @TableField("Sensor_Data_Len")
    private String sensorDataLen;
    /*
  传感器数据
  */
    @TableField("Sensor_Data")
    private  String sensorData;

    /*
    设备电量
     */
    @TableField("Battery_Level")
    private  String batteryLevel;
}
