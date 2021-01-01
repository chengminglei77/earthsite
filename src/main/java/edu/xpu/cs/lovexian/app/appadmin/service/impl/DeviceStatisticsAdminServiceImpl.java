package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminAlarmInfo;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDeviceStatistics;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtus;
import edu.xpu.cs.lovexian.app.appadmin.mapper.CommandInfoAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.DeviceStatisticsAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.DtusAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.UnresovledDataMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IDeviceStatisticsAdminService;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.common.utils.InstructionUtil;
import org.aspectj.apache.bcel.generic.Instruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.awt.print.Book;
import java.util.Date;

/**
 * @author czy
 * @create 2020-10-25-19:47
 */
@Service("deviceStatisticsAdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DeviceStatisticsAdminServiceImpl extends ServiceImpl<DeviceStatisticsAdminMapper, AdminDeviceStatistics> implements IDeviceStatisticsAdminService {
    @Autowired
    private DeviceStatisticsAdminMapper DeviceStatisticsAdminMapper;
    @Autowired
    private UnresovledDataMapper unresovledDataMapper;

    @Override
    public IPage<AdminDeviceStatistics> findDeviceStatisticsByTypeId(QueryRequest request, AdminDeviceStatistics adminDeviceStatistics) {
        QueryWrapper<AdminDeviceStatistics> queryWrapper = new QueryWrapper<>();
        //insertDeviceStatistic("AA550CA8000402012FF26E0755AA0D0A","网关01");
        if (adminDeviceStatistics.getType() != null) {
            queryWrapper.lambda().eq(AdminDeviceStatistics::getType, adminDeviceStatistics.getType()).orderByDesc(AdminDeviceStatistics::getUpdatedAt);
        }
        if (adminDeviceStatistics.getSettingId() != null) {
            queryWrapper.lambda().eq(AdminDeviceStatistics::getSettingId, adminDeviceStatistics.getSettingId()).orderByDesc(AdminDeviceStatistics::getUpdatedAt);
        }
        Page<AdminDeviceStatistics> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);


    }

    @Override
    public void insertDeviceStatistic(String message, String settingId) {
        AdminDeviceStatistics adminDeviceStatistics = DeviceStatisticsAdminMapper.selectDeviceStatistics(settingId);
        Date date = new Date();
        //初始化
        if (adminDeviceStatistics == null) {
            adminDeviceStatistics = new AdminDeviceStatistics();
            adminDeviceStatistics = initializeDeviceStatistic(adminDeviceStatistics, settingId);
            DeviceStatisticsAdminMapper.insert(adminDeviceStatistics);
        }
        Date createTime = DeviceStatisticsAdminMapper.selectCreateTime(InstructionUtil.getTbale(settingId), InstructionUtil.getColum(settingId), settingId);
        if (InstructionUtil.getEqDuration(adminDeviceStatistics.getUpdatedAt(), date) > 1) {
            adminDeviceStatistics.setUpdatedAt(date);
            adminDeviceStatistics.setEqDuration(InstructionUtil.getEqDuration(createTime, adminDeviceStatistics.getUpdatedAt()));
            adminDeviceStatistics.setInfoTotal(unresovledDataMapper.getCount(settingId));
            adminDeviceStatistics.setPacketSize(unresovledDataMapper.getCount(settingId) * InstructionUtil.getDataLength(message));
            UpdateWrapper<AdminDeviceStatistics> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("eq_duration", adminDeviceStatistics.getEqDuration())
                    .set("packet_size", adminDeviceStatistics.getPacketSize())
                    .set("info_total", adminDeviceStatistics.getInfoTotal())
                    .set("updated_at", adminDeviceStatistics.getUpdatedAt())
                    .set("type", InstructionUtil.getType(settingId));
            updateWrapper.eq("setting_id", adminDeviceStatistics.getSettingId());
            DeviceStatisticsAdminServiceImpl.this.update(updateWrapper);
        }

    }

    public AdminDeviceStatistics initializeDeviceStatistic(AdminDeviceStatistics adminDeviceStatistics, String settingId) {
        Date date = new Date();
        //System.out.println("没有现有的对象");
        adminDeviceStatistics.setSettingId(settingId);
        adminDeviceStatistics.setEqDuration(0);
        adminDeviceStatistics.setInfoTotal(0);
        adminDeviceStatistics.setPacketSize(0);
        adminDeviceStatistics.setUpdatedAt(date);
        return adminDeviceStatistics;
    }



    @Override
    public boolean deleteDevice(String id) {
        DeviceStatisticsAdminMapper.deleteById(id);
        return true;
    }
}
