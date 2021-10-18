package edu.xpu.cs.lovexian.phone.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.xpu.cs.lovexian.phone.entity.SensorData;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DS("slave")
public interface SensorInfoMapper extends BaseMapper<SensorData> {
    @Select("SELECT sensor_id , sensor_type FROM sensor_data")
    List<SensorData> findDeviceId();


    @Select("select * from sensor_data")
    List<SensorData> getSensorInfo();
}
