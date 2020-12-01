package edu.xpu.cs.lovexian.app.appadmin.controller;


import edu.xpu.cs.lovexian.app.appadmin.utils.InfluxDbConnection;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
    public void insertOneToInflux(String sensorsAddr,String sensorsType,float sensorsData) {
        Map<String, String> tagsMap = new HashMap<>();
        Map<String, Object> fieldsMap = new HashMap<>();
        tagsMap.put("sensors_id",sensorsAddr);
        tagsMap.put("sensors_type",sensorsType);
        fieldsMap.put("sensors_data",sensorsData);
        influxDBConnection.insert("sensors_data", tagsMap, fieldsMap);
    }



}
