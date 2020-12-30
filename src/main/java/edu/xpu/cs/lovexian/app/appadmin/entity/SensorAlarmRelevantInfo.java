package edu.xpu.cs.lovexian.app.appadmin.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SensorAlarmRelevantInfo {

    private String sensorId;

    private Date colTime;

    private String samplingFrequency;

}
