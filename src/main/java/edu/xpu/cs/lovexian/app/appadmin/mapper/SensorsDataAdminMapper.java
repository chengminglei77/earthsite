package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensorsData;
import org.springframework.stereotype.Component;

/**
 * @author czy
 * @create 2020-11-27-10:18
 */
@Component
@DS("slave")
public interface SensorsDataAdminMapper extends BaseMapper<AdminSensorsData> {
}
