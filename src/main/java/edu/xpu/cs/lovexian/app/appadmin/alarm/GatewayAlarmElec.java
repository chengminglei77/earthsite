package edu.xpu.cs.lovexian.app.appadmin.alarm;

import edu.xpu.cs.lovexian.app.appadmin.entity.AdminAlarmInfo;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGateways;
import edu.xpu.cs.lovexian.app.appadmin.mapper.AlarmInfoAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.GatewaysAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IAlarmInfoAdminService;
import edu.xpu.cs.lovexian.app.appadmin.utils.StatusEnum;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.util.Date;
import java.util.List;


@Configuration("gatewayElecScheduled")
@EnableScheduling
public class GatewayAlarmElec {
    @Autowired
    private GatewaysAdminMapper gatewaysAdminMapper;
    @Autowired
    private IAlarmInfoAdminService alarmInfoAdminService;
    @Autowired
    AlarmInfoAdminMapper alarmInfoAdminMapper;

    AdminAlarmInfo adminAlarmInfo = new AdminAlarmInfo();

    //每30分钟执行一次
    @Scheduled(cron="0 0/30 * * * ?")
    //@Scheduled(cron=" 0/10 * * * * ?")
    public void timmer() throws ParseException {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        ThreadContext.bind(manager);
        List<AdminGateways> adminGatewaysList = gatewaysAdminMapper.selectThelastGatewayInfo();

        for (AdminGateways adminGateways:adminGatewaysList){
            //1.充电状态
            Integer stateOfCharge=adminGateways.getElcStatus();
            //2.当前电量
            int currentCapacity=Integer.parseInt(adminGateways.getElecCharge());
            //3.dtu_id
            String dtuId=adminGateways.getGateId();
            //2.当前时间
            Date currentDate = new Date(System.currentTimeMillis());

            String theId = alarmInfoAdminMapper.checkIfExist(dtuId);
            if (stateOfCharge==0&&currentCapacity<=30&&currentCapacity>10){
                adminAlarmInfo.setId(theId);
                adminAlarmInfo.setDeviceId(dtuId);
                adminAlarmInfo.setAlarmTime(currentDate);
                adminAlarmInfo.setAlarmInfo("网关电量30%预警");
                adminAlarmInfo.setAlarmReason(dtuId+"电量为："+currentCapacity);
                adminAlarmInfo.setStatus(StatusEnum.NORMAL_STATE.getCode());//未删除状态
                alarmInfoAdminService.saveOrUpdate(adminAlarmInfo);
            }
            if (currentCapacity<=10){
                adminAlarmInfo.setId(theId);
                adminAlarmInfo.setDeviceId(dtuId);
                adminAlarmInfo.setAlarmTime(currentDate);
                adminAlarmInfo.setAlarmInfo("网关电量10%警告");
                adminAlarmInfo.setAlarmReason(dtuId+"电量为："+currentCapacity);
                adminAlarmInfo.setStatus(StatusEnum.NORMAL_STATE.getCode());//未删除状态
                alarmInfoAdminService.saveOrUpdate(adminAlarmInfo);
            }
        }
    }
}