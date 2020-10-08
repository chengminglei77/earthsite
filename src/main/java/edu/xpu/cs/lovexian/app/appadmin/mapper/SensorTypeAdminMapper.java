package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensorType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensorType;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Component;

/**
 *  Mapper
 *
 * @author xpu
 * @date 2020-09-27 19:33:26
 */
@Component
@DS("slave")
public interface SensorTypeAdminMapper extends BaseMapper<AdminSensorType> {
    IPage<AdminSensorType> selectAll(Page page, @Param("AdminSensorType") AdminSensorType adminSensorType);

    IPage<AdminSensorType> querySensorTypeInfo(Page page, @Param("AdminSensorType") AdminSensorType adminSensorType);
}
