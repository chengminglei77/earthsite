package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCommandInfo;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDeviceStatistics;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGateways;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * @author czy
 * @create 2020-10-25-19:44
 */
@Component
@DS("slave")
public interface DeviceStatisticsAdminMapper extends BaseMapper<AdminDeviceStatistics> {
    IPage<AdminDeviceStatistics> selectAll(Page page, @Param("adminDeviceStatistics") AdminDeviceStatistics adminDeviceStatistics);
    AdminDeviceStatistics selectDeviceStatistics(@Param("settingId") String settingId);


    Date selectCreateTime(@Param("table") String table,@Param("column") String column, @Param("settingId") String settingId);

}
