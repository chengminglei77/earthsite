package edu.xpu.cs.lovexian.phone.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.xpu.cs.lovexian.phone.entity.SensorData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * (SensorData)表数据库访问层
 *
 * @author makejava
 * @since 2021-05-06 11:36:51
 */
@Component
@DS("slave")
public interface PhoneSensorDataMapper extends BaseMapper<SensorData> {

}