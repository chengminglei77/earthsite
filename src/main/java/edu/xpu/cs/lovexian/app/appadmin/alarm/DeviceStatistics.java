package edu.xpu.cs.lovexian.app.appadmin.alarm;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDeviceStatistics;
import edu.xpu.cs.lovexian.app.appadmin.mapper.DeviceStatisticsAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.UnresovledDataMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IDeviceStatisticsAdminService;
import edu.xpu.cs.lovexian.app.appadmin.service.impl.DeviceStatisticsAdminServiceImpl;
import edu.xpu.cs.lovexian.common.utils.InstructionUtil;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author czy
 * @create 2021-01-04-0:21
 */
@Configuration("DeviceScheduled")
@EnableScheduling
public class DeviceStatistics {
    @Autowired
    private edu.xpu.cs.lovexian.app.appadmin.mapper.DeviceStatisticsAdminMapper DeviceStatisticsAdminMapper;
    @Autowired
    private UnresovledDataMapper unresovledDataMapper;
    @Autowired
    private IDeviceStatisticsAdminService deviceStatisticsAdminService;
    @Scheduled(cron="0 0/30 * * * ?")
    public void timmer() throws ParseException
    { DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        Date date = new Date();
        ThreadContext.bind(manager);
        List<AdminDeviceStatistics> sensors = DeviceStatisticsAdminMapper.getByType("0");
        //更新传感器的统计信息
        for (AdminDeviceStatistics sensor:sensors) {
            String settingId=sensor.getSettingId();
            int count = unresovledDataMapper.getCount(settingId);
            String message = unresovledDataMapper.getMessage(sensor.getSettingId());
            Date createTime = DeviceStatisticsAdminMapper.selectCreateTime(InstructionUtil.getTbale(settingId), InstructionUtil.getColum(settingId), settingId);
            sensor.setUpdatedAt(date);
            sensor.setEqDuration(InstructionUtil.getEqDuration(InstructionUtil.getDayEqDuration(createTime, sensor.getUpdatedAt())));
            sensor.setInfoTotal(InstructionUtil.countTransfer(count));
            sensor.setPacketSize(InstructionUtil.getPrintSize(count * InstructionUtil.getDataLength(message)/4));
            saveStatistic(sensor,settingId);
        }
        //更新DTU的统计信息
        List<AdminDeviceStatistics> Dtus = DeviceStatisticsAdminMapper.getByType("1");
        for (AdminDeviceStatistics dtu:Dtus)
        {       int dtuInfoTotal = 0, dtuPacketSize = 0;
            List<AdminDeviceStatistics> dtuSensors = DeviceStatisticsAdminMapper.getDtuSensors("0", dtu.getSettingId());
            for (AdminDeviceStatistics sensor:dtuSensors) {
                dtuInfoTotal += InstructionUtil.transferCount(sensor.getInfoTotal());
                dtuPacketSize += InstructionUtil.toSize(sensor.getPacketSize());
            }
            String settingId=dtu.getSettingId();
            int count = unresovledDataMapper.getCount(settingId);
            String message = unresovledDataMapper.getMessage(dtu.getSettingId());
            Date createTime = DeviceStatisticsAdminMapper.selectCreateTime(InstructionUtil.getTbale(settingId), InstructionUtil.getColum(settingId), settingId);
            dtu.setUpdatedAt(date);
            dtu.setEqDuration(InstructionUtil.getEqDuration(InstructionUtil.getDayEqDuration(createTime, dtu.getUpdatedAt())));
            dtu.setInfoTotal(InstructionUtil.countTransfer(count+dtuInfoTotal));
            dtu.setPacketSize(InstructionUtil.getPrintSize(count * InstructionUtil.getDataLength(message)/4+dtuPacketSize));
            saveStatistic(dtu,settingId);
        }
        //更新Gatewayes的统计信息
        List<AdminDeviceStatistics> Gateways = DeviceStatisticsAdminMapper.getByType("2");
        for(AdminDeviceStatistics gateway:Gateways)
        {int gatewayInfoTotal = 0, gatewayPacketSize = 0;
            List<AdminDeviceStatistics> dtuGateways = DeviceStatisticsAdminMapper.getDtuSensors("1", gateway.getSettingId());
            for (AdminDeviceStatistics dtu:dtuGateways) {
                gatewayInfoTotal += InstructionUtil.transferCount(dtu.getInfoTotal());
                gatewayPacketSize += InstructionUtil.toSize(dtu.getPacketSize());
            }
            String settingId=gateway.getSettingId();
            int count = unresovledDataMapper.getCount(settingId);
            String message = unresovledDataMapper.getMessage(gateway.getSettingId());
            Date createTime = DeviceStatisticsAdminMapper.selectCreateTime(InstructionUtil.getTbale(settingId), InstructionUtil.getColum(settingId), settingId);
            gateway.setUpdatedAt(date);
            gateway.setEqDuration(InstructionUtil.getEqDuration(InstructionUtil.getDayEqDuration(createTime, gateway.getUpdatedAt())));
            gateway.setInfoTotal(InstructionUtil.countTransfer(count+gatewayInfoTotal));
            gateway.setPacketSize(InstructionUtil.getPrintSize(count * InstructionUtil.getDataLength(message)/4+gatewayPacketSize));
            saveStatistic(gateway,settingId);
        }
    }
    public void saveStatistic(AdminDeviceStatistics adminDeviceStatistics,String settingId)
    {
        UpdateWrapper<AdminDeviceStatistics> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("eq_duration", adminDeviceStatistics.getEqDuration())
                .set("packet_size", adminDeviceStatistics.getPacketSize())
                .set("info_total", adminDeviceStatistics.getInfoTotal())
                .set("updated_at", adminDeviceStatistics.getUpdatedAt())
                .set("type", InstructionUtil.getType(settingId));
        updateWrapper.eq("setting_id", adminDeviceStatistics.getSettingId());
        deviceStatisticsAdminService.update(updateWrapper);

    }
}
