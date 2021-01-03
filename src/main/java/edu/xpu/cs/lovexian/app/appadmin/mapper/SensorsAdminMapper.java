package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Component;

/**
 * Mapper
 *
 * @author xpu
 * @date 2020-09-01 22:02:59
 */
@Component
@DS("slave")
public interface SensorsAdminMapper extends BaseMapper<AdminSensors> {
    IPage<AdminSensors> selectAll(Page page, @Param("AdminSensors") AdminSensors adminSensors);

    IPage<AdminSensors> querySensorsInfo(Page page, @Param("AdminSensors") AdminSensors adminSensors);
}
