package edu.xpu.cs.lovexian.app.appadmin.controller;

import edu.xpu.cs.lovexian.app.appadmin.service.ISensorsAdminService;
import edu.xpu.cs.lovexian.app.appadmin.service.ISensorsDataAdminService;
import edu.xpu.cs.lovexian.common.annotation.Log;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author czy
 * @create 2020-11-27-10:32
 */
public class SensorsDataAdminController extends BaseController {
    @Autowired
    private ISensorsDataAdminService sensorsDataAdminService;

    @Log("sensors管理：查询传感器数据的数据终端设备地址")
    @GetMapping("querySensorAdress")
    public EarthSiteResponse querySensorAdress(String message) {
        String s = sensorsDataAdminService.querySensorAdress(message);
        return EarthSiteResponse.SUCCESS().data(s);
    }
}