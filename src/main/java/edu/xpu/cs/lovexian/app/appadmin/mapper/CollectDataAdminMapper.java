package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCollectData;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;


/**
 * author:zhanganjie
 * date:2020-12-7
 */
@Component
@DS("slave")
public interface CollectDataAdminMapper extends BaseMapper<AdminCollectData> {
    @Select("select sensor_id from sensor_data where sensor_id = #{sensor_id,jdbcType=VARCHAR}")
    String selectId(@Param("sensor_id") String sensor_id);

}
