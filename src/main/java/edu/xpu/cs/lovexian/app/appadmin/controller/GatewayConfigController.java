package edu.xpu.cs.lovexian.app.appadmin.controller;

import edu.xpu.cs.lovexian.app.appadmin.Kafka.KafkaReceiver;
import edu.xpu.cs.lovexian.app.appadmin.Kafka.PerformInstrution;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminAtData;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGatewayConfig;
import edu.xpu.cs.lovexian.app.appadmin.mapper.AtDataMapper;
import edu.xpu.cs.lovexian.app.appadmin.utils.DateUtil;
import edu.xpu.cs.lovexian.common.annotation.Log;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import io.lettuce.core.ScriptOutputType;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.backoff.Sleeper;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author czy
 * @create 2021-04-30-21:22
 */
@Slf4j
@RestController
@RequestMapping("/admin/gatewaysConfig/")
public class GatewayConfigController implements Runnable {


    @Autowired
    AtDataMapper AtDataMapper;

    @ApiOperation(value = "网关配置")
    @Log("网关配置管理:发送下发命令请求")
    @PostMapping("setGatewayConfig")
    public EarthSiteResponse setGatewayConfig(AdminGatewayConfig Command) {
        //插入数据库at_data中，(插入命令和时间)
        AdminAtData adminAtData = new AdminAtData();
        Date date = new Date();
        adminAtData.setCreatedTime(date);
        adminAtData.setAt(Command.getAt());
        AtDataMapper.insert(adminAtData);
        //设置发送的命令
        PerformInstrution.setATcommand(Command.getAt());
        //调用方法，发送数据，请求网 关允许配置管理
        String url = Command.getAscii();
        PerformInstrution.sendInstrution(url);

        //kafka返回数据，到数据库中进行查询
        try {
            System.out.println("执行到这线程停止");
            Thread.sleep(7000);
            System.out.println("执行到这线程停止结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        AdminAtData Data = AtDataMapper.selectLatestData(Command.getAt(), DateUtil.DateToString(date));
        System.out.println("Data为" + Data);
        if (Data != null&&Data.getData()!=null) {
            if (Data.getCreatedTime().equals(date) != true) {
                if (Data.getData().contains("4F4B"))
                    return EarthSiteResponse.SUCCESS().data("OK");
             else  return EarthSiteResponse.SUCCESS().data(Data.getData());
            }
        }
        return EarthSiteResponse.FAIL().data("下发失败");

    }

    @Override
    public void run() {
    }
}
