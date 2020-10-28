package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCommandInfo;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDeviceStatistics;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Component;

/**
 * @author czy
 * @create 2020-10-25-19:44
 */
@Component
@DS("slave")
public interface DeviceStatisticsAdminMapper extends BaseMapper<AdminDeviceStatistics> {
    IPage<AdminDeviceStatistics> selectAll(Page page, @Param("adminDeviceStatistics") AdminDeviceStatistics adminDeviceStatistics);
}
