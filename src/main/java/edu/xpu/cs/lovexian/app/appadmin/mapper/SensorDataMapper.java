package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCollectData;
import edu.xpu.cs.lovexian.app.appadmin.entity.SensorAlarmRelevantInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DS("slave")
public interface SensorDataMapper extends BaseMapper<AdminCollectData> {

    @Select("select sensor_data.sensor_id,sensor_data.col_time,sensors.sampling_frequency from sensor_data join sensors on sensors.sensor_id=sensor_data.sensor_id")
    List<SensorAlarmRelevantInfo> selectThelastSensorInfoTime();

}
