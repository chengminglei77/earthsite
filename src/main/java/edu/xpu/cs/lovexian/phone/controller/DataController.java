package edu.xpu.cs.lovexian.phone.controller;

import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.phone.entity.Dtus;
import edu.xpu.cs.lovexian.phone.entity.Gateways;
import edu.xpu.cs.lovexian.phone.entity.Result;
import edu.xpu.cs.lovexian.phone.entity.SensorData;
import edu.xpu.cs.lovexian.phone.service.DtusService;
import edu.xpu.cs.lovexian.phone.service.GatewaysService;
import edu.xpu.cs.lovexian.phone.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author czy
 * @create 2021-05-06-11:13
 */
@RestController
@RequestMapping("/getData")
public class DataController {
@Autowired
    DtusService dtusService;
@Autowired
    GatewaysService gatewaysService;
@Autowired
    SensorDataService sensorDataService;
    @GetMapping("/getAllData")
    public EarthSiteResponse getAllData()
    {
        List<Dtus> dtuData = dtusService.list();
        List<Gateways> gatewaysData = gatewaysService.list();
        List<SensorData> sensorsData = sensorDataService.list();
     return   EarthSiteResponse.SUCCESS().put("dtuData", dtuData)
                                         .put("gatewaysData",gatewaysData)
                                         .put("sensorsData",sensorsData);


    }
}
