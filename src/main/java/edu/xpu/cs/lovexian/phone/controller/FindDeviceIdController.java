package edu.xpu.cs.lovexian.phone.controller;

import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCollectData;
import edu.xpu.cs.lovexian.phone.service.SensorInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zaj
 *
 */
@RestController
@RequestMapping("/settingId")
public class FindDeviceIdController {

    @Autowired
    SensorInfoService sensorInfoService;

    @ApiOperation(value = "查找风速和湿度的设备id")
    @GetMapping("getSettingId")
    public Map<String,Object> getSettingId(){
        List<AdminCollectData> sensorDataAdmins = new ArrayList<>(19);
        sensorDataAdmins = sensorInfoService.findDeviceId();
        List<String> names = sensorDataAdmins.stream().map(p -> p.getSensorId())
                .collect(Collectors.toList());
        List<String> list1 = names.subList(0,4);
        List<String> list2 = names.subList(4,19);
        Map map = new HashMap();
        map.put("风速传感器",list1);
        map.put("湿度传感器",list2);;
        return map;
    }

    @ApiOperation(value = "查找传感器的设备信息")
    @GetMapping("getSensorInfo")
    public List<AdminCollectData> getSensorInfo(){
        return sensorInfoService.getSensorInfo();
    }
}
