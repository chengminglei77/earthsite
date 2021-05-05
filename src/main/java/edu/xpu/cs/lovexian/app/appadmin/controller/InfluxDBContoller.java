package edu.xpu.cs.lovexian.app.appadmin.controller;


import edu.xpu.cs.lovexian.app.appadmin.utils.InfluxDbConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * User: 马鹏森 2020-09-25-9:26
 */
@RestController
public class InfluxDBContoller {

    //需要使用的地方直接注入
    @Autowired
    InfluxDbConnection influxDBConnection;

    @PostMapping("/insertOneToInfluxDB")
    public void insertOneToInflux(String sensorAddr, String sensorType, double windSpeed_10min) {
        Map<String, String> tagsMap = new HashMap<>();
        Map<String, Object> fieldsMap = new HashMap<>();
        tagsMap.put("sensor_id", sensorAddr);
        tagsMap.put("sensor_type", sensorType);
        fieldsMap.put("sensor_data", windSpeed_10min);
        influxDBConnection.insert("sensor_data", tagsMap, fieldsMap);
    }

    @PostMapping("/insertTwoToInfluxDB")
    public void insertTwoToInfluxDB(String Device_ID, String change, float Battery_Level) {
        Map<String, String> tagsMap = new HashMap<>();
        Map<String, Object> fieldsMap = new HashMap<>();
        tagsMap.put("Device_ID", Device_ID);
        tagsMap.put("Change ", change);
        fieldsMap.put("Battery_Level", Battery_Level);
        influxDBConnection.insert("Battery_Level", tagsMap, fieldsMap);

    }
}
