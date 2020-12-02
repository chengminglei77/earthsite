package edu.xpu.cs.lovexian.app.appadmin.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensorsData;

/**
 * @author czy
 * @create 2020-11-27-10:19
 */
@DS("slave")
public interface ISensorsDataAdminService extends IService<AdminSensorsData> {
    void querySensorAdress(String message) throws Exception;

    public String setSensorAddrAndType(String message);

    public String reportSensorAddrAndTypeAndNum(String message);

    public String deleteSensor(String message);

    void setSensorReportTime(String message);
    void getSensorReportTime(String message);
    void ReportSensorDataCommand(String message);
}
