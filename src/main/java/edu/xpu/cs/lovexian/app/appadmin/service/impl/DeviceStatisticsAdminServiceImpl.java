package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminAlarmInfo;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDeviceStatistics;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtus;
import edu.xpu.cs.lovexian.app.appadmin.mapper.CommandInfoAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.DeviceStatisticsAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.DtusAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IDeviceStatisticsAdminService;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author czy
 * @create 2020-10-25-19:47
 */
@Service("deviceStatisticsAdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeviceStatisticsAdminServiceImpl  extends ServiceImpl<DeviceStatisticsAdminMapper, AdminDeviceStatistics> implements IDeviceStatisticsAdminService {
    @Autowired
    private DeviceStatisticsAdminMapper commandInfoAdminMapper;

    @Override
    public IPage<AdminDeviceStatistics> findDeviceStatisticsByTypeId(QueryRequest request, AdminDeviceStatistics adminDeviceStatistics) {
        QueryWrapper<AdminDeviceStatistics> queryWrapper = new QueryWrapper<>();
        if(adminDeviceStatistics.getType()!=null)
        {
            queryWrapper.lambda().eq(AdminDeviceStatistics::getType, adminDeviceStatistics.getType());
        }
        if(adminDeviceStatistics.getSettingId()!=null)
        {
            queryWrapper.lambda().eq(AdminDeviceStatistics::getSettingId,adminDeviceStatistics.getSettingId());
        }
        Page<AdminDeviceStatistics> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean deleteDevice(String id) {
        commandInfoAdminMapper.deleteById(id);
        return true;
    }
}
