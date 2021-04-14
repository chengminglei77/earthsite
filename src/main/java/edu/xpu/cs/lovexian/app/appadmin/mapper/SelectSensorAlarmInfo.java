package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminAlarmInfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DS("slave")
public interface SelectSensorAlarmInfo extends BaseMapper<AdminAlarmInfo> {
    @Select("select * from alarm_info where left(alarm_info.device_id, 1)<>'G'")
    List<AdminAlarmInfo> selectSensorAlarmInfo();
}
