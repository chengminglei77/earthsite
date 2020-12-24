package edu.xpu.cs.lovexian.app.appadmin.alarm;

import edu.xpu.cs.lovexian.app.appadmin.entity.AdminAlarmInfo;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtus;
import edu.xpu.cs.lovexian.app.appadmin.mapper.DtusAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IAlarmInfoAdminService;
import edu.xpu.cs.lovexian.app.appadmin.utils.StatusEnum;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Configuration("dtuScheduled")
@EnableScheduling
public class DtuAlarm {

    @Autowired
    private DtusAdminMapper dtusAdminMapper;
    @Autowired
    private IAlarmInfoAdminService alarmInfoAdminService;

    //每30分钟执行一次
    @Scheduled(cron="0 0/30 * * * ?")
    public void timmer() throws ParseException {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        ThreadContext.bind(manager);

        AdminDtus adminDtus = dtusAdminMapper.selectThelastDtuInfo();
        //1.充电状态
        Integer stateOfCharge=adminDtus.getElcStatus();
        //2.当前电量
        int currentCapacity=Integer.parseInt(adminDtus.getElecCharge());
        //3.最新一条数据时间
        Date theLastData=adminDtus.getCreatedAt();
        //4.dtu_id
        String dtuId=adminDtus.getDtuId();


        //1.采集到的最后一条数据时间
        long from3 = theLastData.getTime();
        //2.当前时间
        Date currentDate = new Date(System.currentTimeMillis());
        Date toDate3 = currentDate;
        long to3 = toDate3.getTime();
        //3.时间差
        int minutes = (int) ((to3 - from3) / (1000 * 60));

        if (minutes>=15){
            AdminAlarmInfo adminAlarmInfo = new AdminAlarmInfo();
            adminAlarmInfo.setAlarmTime(currentDate);
            adminAlarmInfo.setAlarmInfo("DTU设备"+dtuId+"超过15分钟未收到数据");
            adminAlarmInfo.setAlarmReason("最后一次收到数据的时间是："+theLastData);
            adminAlarmInfo.setStatus(StatusEnum.NORMAL_STATE.getCode());//未删除状态
            alarmInfoAdminService.saveOrUpdate(adminAlarmInfo);
        }
        if (stateOfCharge==0&&currentCapacity<=10000){
            AdminAlarmInfo adminAlarmInfo = new AdminAlarmInfo();
            adminAlarmInfo.setAlarmTime(currentDate);
            adminAlarmInfo.setAlarmInfo("DTU设备"+dtuId+"电压小于10V且未充电");
            adminAlarmInfo.setAlarmReason(dtuId+"电压小于10V且未充电，电压为："+currentCapacity);
            adminAlarmInfo.setStatus(StatusEnum.NORMAL_STATE.getCode());//未删除状态
            alarmInfoAdminService.saveOrUpdate(adminAlarmInfo);
        }












    }
}
