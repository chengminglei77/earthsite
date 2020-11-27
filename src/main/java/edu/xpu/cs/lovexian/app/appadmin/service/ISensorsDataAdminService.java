package edu.xpu.cs.lovexian.app.appadmin.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensorsData;

/**
 * @author czy
 * @create 2020-11-27-10:19
 */
@DS("slave")
public interface ISensorsDataAdminService extends IService<AdminSensorsData> {
    String querySensorAdress(String message);
}
