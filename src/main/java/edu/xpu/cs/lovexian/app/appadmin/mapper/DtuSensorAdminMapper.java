package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtuSensor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 *  Mapper
 *
 * @author xpu
 * @date 2020-09-21 16:34:55
 */
@Component
@DS("slave")
public interface DtuSensorAdminMapper extends BaseMapper<AdminDtuSensor> {
    IPage<AdminDtuSensor> selectCheckInfos(Page page, @Param("adminDtuSensor") AdminDtuSensor adminDtuSensor);

}
