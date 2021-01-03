package edu.xpu.cs.lovexian.app.appadmin.Kafka;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import edu.xpu.cs.lovexian.app.appadmin.controller.InfluxDBContoller;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCollectData;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminUnresovledData;
import edu.xpu.cs.lovexian.app.appadmin.mapper.CollectDataAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.GatewayDtuAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.UnresovledDataMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IDeviceStatisticsAdminService;
import edu.xpu.cs.lovexian.common.utils.HumidityUtils;
import edu.xpu.cs.lovexian.common.utils.InstructionUtil;
import edu.xpu.cs.lovexian.common.utils.TransferVUtil;
import edu.xpu.cs.lovexian.common.utils.WindSpeedUtils;
import lombok.Data;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @author czy
 * @create 2020-12-31-15:10
 */
@Component
@Data
public class PerformInstrution {
    @Autowired
    InfluxDBContoller influxDBContoller;
    @Autowired
    UnresovledDataMapper unresovledDataMapper;
    @Autowired
    IDeviceStatisticsAdminService deviceStatisticsAdminService;
    @Autowired
    GatewayDtuAdminMapper gatewayDtuAdminMapper;
    @Autowired
    CollectDataAdminMapper collectDataAdminMapper;

    public static Logger log = Logger.getLogger(KafkaReceiver.class);

    public void performA6(String message){
        Date coltTime = new Date();
        //java.sql.Date colTime = new java.sql.Date(new java.util.Date().getTime());
        String deviceId = message.substring(12, 14);
        String sensorsType[] = InstructionUtil.getSensorType(message);
        String sensorsAddr = message.substring(16, 20);

        double [] averageSpeed = WindSpeedUtils.windSpeed(message);
        String sensorId[] = getSensorSettingId(message);
        String [] windDirection = WindSpeedUtils.windDirection(message);
        double [] humidity = HumidityUtils.humidityDecode(message);
        String [] id = {"1344101086971305986","1344101086971305987"};
        if (sensorsType[0].equals("风速传感器")) {
            for (int i=0;i<averageSpeed.length;i++){
                influxDBContoller.insertOneToInflux(sensorId[0]+"0"+i,sensorsType[0],(double) Math.round(averageSpeed[i]*100)/100);
            }
            for (int i=0;i<averageSpeed.length;i++){
                LambdaUpdateWrapper<AdminCollectData> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(AdminCollectData::getId,id[i])
                        .set(AdminCollectData::getSensorType, sensorsType[0])
                        .set(AdminCollectData::getSensorId,sensorId[0]+"0"+i)
                        .set(AdminCollectData::getSensorValue, (double) Math.round(averageSpeed[i]*100)/100)
                        .set(AdminCollectData::getSensorParam,windDirection[i])
                        .set(AdminCollectData::getColTime, coltTime);
                Integer rows = collectDataAdminMapper.update(null, lambdaUpdateWrapper);
            }

        }else {
            for (int i=0;i<humidity.length;i++){
                influxDBContoller.insertOneToInflux(sensorId[0],sensorsType[0],(double) Math.round(humidity[i]*100)/100);
            }
        }
    }

    public void performA7(String Message) {
        String ack = InstructionUtil.getAck(Message);
        String deviceId = InstructionUtil.getDeviceId(Message);
        if (ack.equals("01"))
            log.error("确认帧错误，失败");
        if (ack.equals("02"))
            log.error("CRC校验失败");
        if (ack.equals("00"))
            log.info("指令解析成功，数据终端设备地址为" + deviceId);
    }

    public void performA8(String Message) {
        log.info("A8:" + Message);
        String deviceId = InstructionUtil.getDeviceId(Message);
        String change = InstructionUtil.getChange(Message);
        float batteryLevel = InstructionUtil.getBatteryLevel(Message);
        int Percentage = TransferVUtil.encrypt(batteryLevel);
        float batteryPercentage = Percentage / 100.0f;
        System.out.println("电量百分比为" + batteryPercentage);
        influxDBContoller.insertTwoToInfluxDB(deviceId, change, batteryPercentage);
        //插入到MYSQL数据库A6_data
        Date time = new Date();
        AdminUnresovledData adminUnresovledData1 = new AdminUnresovledData();
        adminUnresovledData1.setData(Message.toString());
        adminUnresovledData1.setSensorType(deviceId);
        adminUnresovledData1.setSensorData(String.valueOf(batteryLevel));
        adminUnresovledData1.setInstructionType("A8");
        String settingId = getDtuOrGatewaySettingId(deviceId);
        adminUnresovledData1.setColTime(time);
        adminUnresovledData1.setSettingID(settingId);
        unresovledDataMapper.insert(adminUnresovledData1);
        //插入数据统计中
        deviceStatisticsAdminService.insertDeviceStatistic(Message.toString(), settingId);
    }

    public void performA9(String Message) {
        String ack = InstructionUtil.getAck(Message);
        String deviceId = InstructionUtil.getDeviceId(Message);
        String change = InstructionUtil.getChange(Message);
        float batteryLevel = InstructionUtil.getBatteryLevel(Message);
        if (ack.equals("01"))
            log.error("确认帧错误，失败");
        if (ack.equals("02"))
            log.error("CRC校验失败");
        if (ack.equals("00"))
            log.info("指令解析成功，数据终端设备地址为" + deviceId);
        log.info("数据终端设备电量为：" + batteryLevel);
        log.info("数据终端设备的充电状态为：" + change);
    }

    /**
     * 获取DTU或者Gateway的SettingId
     *
     * @param deviceId
     * @return
     */
    public String getDtuOrGatewaySettingId(String deviceId) {

        if (deviceId.equals("01"))//增加ff作为第二个网关地址
            return "GW" + deviceId;
        String settingId = "DTU" + deviceId;
        String gatewaySettingId = gatewayDtuAdminMapper.selectGatewaySettingId(settingId);
        return gatewaySettingId + settingId;//判空
    }

    /**
     * 获取传感器的SettingId
     *
     * @param message
     * @return
     */
    public  String[] getSensorSettingId(String message) {
        String deviceId = InstructionUtil.getDeviceId(message);
        String dtuOrGatewaySettingId = getDtuOrGatewaySettingId(deviceId);
        String[] sensorType = InstructionUtil.getSensorType(message);
        String[] sensorAddress = InstructionUtil.getSensorAddress(message);
        String[] sensorSettingId = new String[sensorType.length];
        for (int i = 0; i < sensorType.length; i++) {
            if (sensorType[i].equals("湿度传感器"))
                sensorSettingId[i] = "ST" + dtuOrGatewaySettingId + "01" + sensorAddress[i];
            if (sensorType[i].equals("风速传感器"))
                sensorSettingId[i] = "FS" + dtuOrGatewaySettingId + "02" + sensorAddress[i];
            if (sensorType[i].equals("水盐传感器"))
                sensorSettingId[i] = "WS" + dtuOrGatewaySettingId + "03" + sensorAddress[i];
        }
        return sensorSettingId;
    }

    public static void main(String[] args) {
        PerformInstrution performInstrution = new PerformInstrution();
        String[] settingId = performInstrution.getSensorSettingId("AA552AA6001F020200011A004A0000006A016400510150003400E1002B00CB002500C100129DA655AA");
        for (int i = 0; i <settingId.length ; i++) {
            System.out.println(settingId[i]);
        }
    }
}
