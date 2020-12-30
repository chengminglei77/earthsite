package edu.xpu.cs.lovexian.app.appadmin.alarm;

import edu.xpu.cs.lovexian.app.appadmin.entity.*;
import edu.xpu.cs.lovexian.app.appadmin.mapper.SensorDataMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IAlarmInfoAdminService;
import edu.xpu.cs.lovexian.app.appadmin.utils.StatusEnum;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Configuration("gatewayScheduled")
@EnableScheduling
public class DtuAlarmTime {

    @Autowired
    private SensorDataMapper sensorDataMapper;
    @Autowired
    private IAlarmInfoAdminService alarmInfoAdminService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    AdminAlarmInfo adminAlarmInfo = new AdminAlarmInfo();
    //每30分钟执行一次
    //@Scheduled(cron="0 0/30 * * * ?")

    @Scheduled(cron=" 0/10 * * * * ?")

    public void timmer() throws ParseException {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        ThreadContext.bind(manager);

        List<SensorAlarmRelevantInfo> sensorAlarmRelevantInfoList = sensorDataMapper.selectThelastSensorInfoTime();
        System.out.println(sensorAlarmRelevantInfoList);
        for(SensorAlarmRelevantInfo sensorAlarmRelevantInfo:sensorAlarmRelevantInfoList){
            //3.最新一条数据时间
            Date theLastData=sensorAlarmRelevantInfo.getColTime();
            String currentTime = sdf.format(theLastData);

            //4.gateway_id
            String sensorId=sensorAlarmRelevantInfo.getSensorId();
            //得到采集频率
            int samplingFrequency=Integer.parseInt(sensorAlarmRelevantInfo.getSamplingFrequency());

            //1.采集到的最后一条数据时间
            long from3 = theLastData.getTime();

            //2.当前时间
            Date currentDate = new Date(System.currentTimeMillis());
            Date toDate3 = currentDate;
            long to3 = toDate3.getTime();
            //3.时间差
            int minutes = (int) ((to3 - from3) / (1000 * 60));

            if (minutes>=(samplingFrequency+1)){
                adminAlarmInfo.setDeviceId(sensorId);
                adminAlarmInfo.setAlarmTime(currentDate);
                adminAlarmInfo.setAlarmInfo("传感器"+sensorId+"超过"+samplingFrequency+"分钟未收到数据");
                adminAlarmInfo.setAlarmReason(sensorId+"最后一次收到数据的时间是："+currentTime);
                adminAlarmInfo.setStatus(StatusEnum.NORMAL_STATE.getCode());//未删除状态
                alarmInfoAdminService.saveOrUpdate(adminAlarmInfo);
            }
        }

    }
}
