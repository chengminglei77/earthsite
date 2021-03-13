package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminAlarmInfo;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtus;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGatewayDtu;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper
 *
 * @author xpu
 * @date 2020-09-21 19:42:21
 */
@Component
@DS("slave")
public interface AlarmInfoAdminMapper extends BaseMapper<AdminAlarmInfo> {
    IPage<AdminAlarmInfo> selectAll(Page page, @Param("adminAlarmInfo") AdminAlarmInfo adminAlarmInfo);

    IPage<AdminAlarmInfo> queryAlarmInfo(Page page, @Param("adminAlarmInfo") AdminAlarmInfo adminAlarmInfo);

    //查询网关历史报警信息
    @Select("select device_id, alarm_info,alarm_time from alarm_info where device_id = #{gateId} ORDER BY alarm_time")
    IPage<AdminAlarmInfo> gatewayAlarmInfoHistory(Page page, @Param("gateId") String gateId);

    //查询传感器历史报警信息
    @Select("select device_id, alarm_info,alarm_time from alarm_info where device_id like CONCAT('%',CONCAT(#{sensorId,jdbcType=VARCHAR},'%')) ORDER BY alarm_time DESC")
    List<AdminAlarmInfo> sensorsAlarmInfoHistory(@Param("sensorId") String sensorId);

    //查询dtu历史报警信息
    IPage<AdminAlarmInfo> dtuAlarmInfoHistory(Page page, @Param("dtuId") String dtuId);

    @Select("select id from alarm_info where device_id=#{deviceId,jdbcType=VARCHAR}")
    String checkIfExist(@Param("deviceId") String deviceId);


}
