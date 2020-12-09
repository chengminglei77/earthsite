package edu.xpu.cs.lovexian.app.appadmin.controller;


import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCollectData;
import edu.xpu.cs.lovexian.app.appadmin.mapper.CollectDataAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.utils.CollectData;
import edu.xpu.cs.lovexian.app.appadmin.utils.InfluxDbConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * author:zhanganjie
 */

@RestController
public class CollectDataController {

    @Autowired
    CollectDataAdminMapper collectDataAdminMapper;

    @PostMapping("/collectData")
    public void CollectData(){
        AdminCollectData data = new AdminCollectData();
        String sensorId = "1234";
        String sensorType = "水盐传感器";
        double sensorValue = 15522;
        String colTime = "15";
        data.setSensorType(sensorType);
        data.setSensorId(sensorId);
        data.setSensorValue(sensorValue);
        data.setColTime(colTime);
        collectDataAdminMapper.insert(data);
    }
}
