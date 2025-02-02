package edu.xpu.cs.lovexian.app.appadmin.alarm;

import edu.xpu.cs.lovexian.app.appadmin.entity.AdminAlarmInfo;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtus;
import edu.xpu.cs.lovexian.app.appadmin.mapper.AlarmInfoAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.DtusAdminMapper;
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
import java.util.UUID;


@Configuration("dtuScheduled")
@EnableScheduling
public class DtuAlarmElec {
    @Autowired
    private DtusAdminMapper dtusAdminMapper;
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
        List<AdminDtus> adminDtusList = dtusAdminMapper.selectThelastDtuInfo();

        for (AdminDtus adminDtus:adminDtusList){
            //1.充电状态
            Integer stateOfCharge=adminDtus.getElcStatus();
            //2.当前电量
            int currentCapacity=Integer.parseInt(adminDtus.getElecCharge());
            //3.dtu_id
            String dtuId=adminDtus.getDtuId();
            //2.当前时间
            Date currentDate = new Date(System.currentTimeMillis());

            //String theId = alarmInfoAdminMapper.checkIfExist(dtuId);
            if (stateOfCharge==0&&currentCapacity<=50&&currentCapacity>30){
                UUID uuid=UUID.randomUUID();
                String uuidStr=uuid.toString();
                adminAlarmInfo.setId(uuidStr);
                adminAlarmInfo.setDeviceId(dtuId);
                adminAlarmInfo.setAlarmTime(currentDate);
                adminAlarmInfo.setAlarmInfo("DTU电量50%预警");
                adminAlarmInfo.setAlarmReason(dtuId+"电量为："+currentCapacity);
                adminAlarmInfo.setStatus(StatusEnum.NORMAL_STATE.getCode());//未删除状态
                alarmInfoAdminService.save(adminAlarmInfo);
                //alarmInfoAdminService.saveOrUpdate(adminAlarmInfo);

            }
            if (currentCapacity<=30){
                UUID uuid=UUID.randomUUID();
                String uuidStr=uuid.toString();
                adminAlarmInfo.setId(uuidStr);
                adminAlarmInfo.setDeviceId(dtuId);
                adminAlarmInfo.setAlarmTime(currentDate);
                adminAlarmInfo.setAlarmInfo("DTU电量30%警告");
                adminAlarmInfo.setAlarmReason(dtuId+"电量为："+currentCapacity);
                adminAlarmInfo.setStatus(StatusEnum.NORMAL_STATE.getCode());//未删除状态
                alarmInfoAdminService.save(adminAlarmInfo);
            }
        }
   }
}
