package edu.xpu.cs.lovexian.app.appadmin.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: zaj
 * @Date: 2021/10/12/21:34
 * @Description:
 */
@Data
public class WindSpeedCount {
    private String id;

    private String sensorData;

    private String sensorId;

    private String windSpeedCount0;

    private String windSpeedCount1;

    private String windSpeedCount2;

    private String windSpeedCount3;

    private String windSpeedCount4;

    private String windSpeedCount5;

    private Date colTime;

}
