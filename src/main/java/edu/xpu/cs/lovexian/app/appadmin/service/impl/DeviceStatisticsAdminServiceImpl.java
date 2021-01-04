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
import java.util.List;

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
    /*    Date createTime = DeviceStatisticsAdminMapper.selectCreateTime(InstructionUtil.getTbale(settingId), InstructionUtil.getColum(settingId), settingId);
         {
            {
                switch (InstructionUtil.getType(settingId)) {
                    case "0":
                        adminDeviceStatistics = getSensorsDeviceStatistic(adminDeviceStatistics, settingId, message, date, createTime);
                        break;
                    case "1":
                        adminDeviceStatistics = getDtusDeviceStatistic(adminDeviceStatistics, settingId, message, date, createTime);
                        break;
                    case "2":
                        adminDeviceStatistics = getGatewayDeviceStatistic(adminDeviceStatistics, settingId, message, date, createTime);
                        break;
                }
            }

            saveStatistic(adminDeviceStatistics,settingId);
        }*/

    }

    /**
     * 如果数据库中没有这条信息，则插入
     *
     * @param adminDeviceStatistics
     * @param settingId
     * @return
     */

    public AdminDeviceStatistics initializeDeviceStatistic(AdminDeviceStatistics adminDeviceStatistics, String settingId) {
        Date date = new Date();
        adminDeviceStatistics.setSettingId(settingId);
        adminDeviceStatistics.setEqDuration("0年0月0天");
        adminDeviceStatistics.setInfoTotal("0条");
        adminDeviceStatistics.setPacketSize("0B");
        adminDeviceStatistics.setType(InstructionUtil.getType(settingId));
        adminDeviceStatistics.setUpdatedAt(date);
        return adminDeviceStatistics;
    }

    /**
     * 获取传感器的信息量条数和信息量大小
     *
     * @param adminDeviceStatistics
     * @param settingId
     * @param message
     * @param date
     * @param createTime
     * @return
     */
    public AdminDeviceStatistics getSensorsDeviceStatistic(AdminDeviceStatistics adminDeviceStatistics, String settingId, String message, Date date, Date createTime) {
        adminDeviceStatistics.setUpdatedAt(date);
        adminDeviceStatistics.setEqDuration(InstructionUtil.getEqDuration(InstructionUtil.getDayEqDuration(createTime, adminDeviceStatistics.getUpdatedAt())));
        adminDeviceStatistics.setInfoTotal(InstructionUtil.countTransfer(unresovledDataMapper.getCount(settingId)));
        adminDeviceStatistics.setPacketSize(InstructionUtil.getPrintSize(unresovledDataMapper.getCount(settingId) * InstructionUtil.getDataLength(message)/4));
        return adminDeviceStatistics;
    }

    /**
     * 获取Dtus的信息量总数和信息量大小
     *
     * @param adminDeviceStatistics
     * @param settingId
     * @param message
     * @param date
     * @param createTime
     * @return
     */
    public AdminDeviceStatistics getDtusDeviceStatistic(AdminDeviceStatistics adminDeviceStatistics, String settingId, String message, Date date, Date createTime) {

        int dtuInfoTotal = 0, dtuPacketSize = 0;
        List<AdminDeviceStatistics> sensors = DeviceStatisticsAdminMapper.selectDtuSensors(settingId);
        for (AdminDeviceStatistics sensor : sensors) {
            dtuInfoTotal += InstructionUtil.transferCount(sensor.getInfoTotal());
            dtuPacketSize += InstructionUtil.toSize(sensor.getPacketSize());
        }
        adminDeviceStatistics.setEqDuration(InstructionUtil.getEqDuration(InstructionUtil.getDayEqDuration(createTime, adminDeviceStatistics.getUpdatedAt())));
        adminDeviceStatistics.setInfoTotal(InstructionUtil.countTransfer(unresovledDataMapper.getCount(settingId)+ dtuInfoTotal) );
        adminDeviceStatistics.setPacketSize(InstructionUtil.getPrintSize(unresovledDataMapper.getCount(settingId) * InstructionUtil.getDataLength(message) + dtuPacketSize));
        return adminDeviceStatistics;
    }

    /**
     * 获取Gateway的信息量条数和信息量大小
     *
     * @param adminDeviceStatistics
     * @param settingId
     * @param message
     * @param date
     * @param createTime
     * @return
     */
    public AdminDeviceStatistics getGatewayDeviceStatistic(AdminDeviceStatistics adminDeviceStatistics, String settingId, String message, Date date, Date createTime) {
        int gatewayInfoTotal = 0, gatewayPacketSize = 0;
        List<AdminDeviceStatistics> dtus = DeviceStatisticsAdminMapper.selectgatewayDtus(settingId);
        for (AdminDeviceStatistics dtu : dtus) {
            gatewayInfoTotal += InstructionUtil.transferCount(dtu.getInfoTotal());
            gatewayPacketSize += InstructionUtil.toSize(dtu.getPacketSize());
        }
        adminDeviceStatistics.setEqDuration(InstructionUtil.getEqDuration(InstructionUtil.getDayEqDuration(createTime, adminDeviceStatistics.getUpdatedAt())));
        adminDeviceStatistics.setInfoTotal(InstructionUtil.countTransfer(unresovledDataMapper.getCount(settingId) + gatewayInfoTotal));
        adminDeviceStatistics.setPacketSize(InstructionUtil.getPrintSize(unresovledDataMapper.getCount(settingId) * InstructionUtil.getDataLength(message) + gatewayPacketSize));
        return adminDeviceStatistics;
    }

    //插入数据库中
    public void saveStatistic(AdminDeviceStatistics adminDeviceStatistics,String settingId)
    {UpdateWrapper<AdminDeviceStatistics> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("eq_duration", adminDeviceStatistics.getEqDuration())
                .set("packet_size", adminDeviceStatistics.getPacketSize())
                .set("info_total", adminDeviceStatistics.getInfoTotal())
                .set("updated_at", adminDeviceStatistics.getUpdatedAt())
                .set("type", InstructionUtil.getType(settingId));
        updateWrapper.eq("setting_id", adminDeviceStatistics.getSettingId());
        DeviceStatisticsAdminServiceImpl.this.update(updateWrapper);

    }
    @Override
    public boolean deleteDevice(String id) {
        DeviceStatisticsAdminMapper.deleteById(id);
        return true;
    }

}
