package edu.xpu.cs.lovexian.phone.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCollectData;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SensorInfoMapper extends BaseMapper<AdminCollectData> {
    @Select("SELECT sensor_id FROM sensor_data")
    List<AdminCollectData> findDeviceId();

    @Select("select * from sensor_data")
    List<AdminCollectData> getSensorInfo();
}
