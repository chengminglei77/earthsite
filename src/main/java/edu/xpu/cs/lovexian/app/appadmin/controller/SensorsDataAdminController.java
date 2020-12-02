package edu.xpu.cs.lovexian.app.appadmin.controller;

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

   /* @Log("sensors管理：查询传感器数据的数据终端设备地址")
    @GetMapping("querySensorAdress")
    public EarthSiteResponse querySensorAdress(String message) throws Exception {
        String s = sensorsDataAdminService.querySensorAdress(message);
        return EarthSiteResponse.SUCCESS().data(s);
    }*/
    @Log("sensors管理：设置上报数据的传感器类型和传感器地址")
    @GetMapping("setSensorAddrAndType")
    public EarthSiteResponse setSensorAddrAndType(String message) {
        String s = sensorsDataAdminService.setSensorAddrAndType(message);
        return EarthSiteResponse.SUCCESS().data(s);
    }
    @Log("sensors管理：获取当前上报数据的传感器类型和传感器地址以及传感器个数")
    @GetMapping("reportSensorAddrAndTypeAndNum")
    public EarthSiteResponse reportSensorAddrAndTypeAndNum(String message) {
        String s = sensorsDataAdminService.reportSensorAddrAndTypeAndNum(message);
        return EarthSiteResponse.SUCCESS().data(s);
    }
    @Log("sensors管理：删除某个上报数据的传感器")
    @GetMapping("deleteSensor")
    public EarthSiteResponse deleteSensor(String message) {
        String s = sensorsDataAdminService.deleteSensor(message);
        return EarthSiteResponse.SUCCESS().data(s);
    }

    @Log("sensors管理：设置传感器上报数据时间：0xA4")
    @GetMapping("setSensorReportTime")
    public EarthSiteResponse setSensorReportTime(String message) {
        String s = sensorsDataAdminService.setSensorReportTime(message);
        return EarthSiteResponse.SUCCESS().data(s);
    }

    @Log("sensors管理：获取传感器上报数据时间：0xA5")
    @GetMapping("getSensorReportTime")
    public EarthSiteResponse getSensorReportTime(String message) {
        String s = sensorsDataAdminService.getSensorReportTime(message);
        return EarthSiteResponse.SUCCESS().data(s);
    }

    @Log("sensors管理：上报传感器数据指令：0xA6")
    @GetMapping("ReportSensorDataCommand")
    public EarthSiteResponse ReportSensorDataCommand(String message) {
        String s = sensorsDataAdminService.ReportSensorDataCommand(message);
        return EarthSiteResponse.SUCCESS().data(s);
    }
}