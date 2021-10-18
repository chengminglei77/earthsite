package edu.xpu.cs.lovexian.app.appadmin.Kafka;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.xpu.cs.lovexian.app.appadmin.controller.InfluxDBContoller;
import edu.xpu.cs.lovexian.app.appadmin.entity.*;
import edu.xpu.cs.lovexian.app.appadmin.mapper.*;
import edu.xpu.cs.lovexian.app.appadmin.service.ICommandInfoAdminService;
import edu.xpu.cs.lovexian.app.appadmin.service.IDeviceStatisticsAdminService;
import edu.xpu.cs.lovexian.app.appadmin.service.IDtusAdminService;
import edu.xpu.cs.lovexian.app.appadmin.service.IGatewaysAdminService;
import edu.xpu.cs.lovexian.app.constant.MessageAckEnum;
import edu.xpu.cs.lovexian.common.utils.*;
import lombok.Data;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;


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
    CommandInfoAdminMapper commandInfoAdminMapper;
    @Autowired
    private ICommandInfoAdminService commandInfoAdminService;
    @Autowired
    IDeviceStatisticsAdminService deviceStatisticsAdminService;
    @Autowired
    GatewayDtuAdminMapper gatewayDtuAdminMapper;
    @Autowired
    CollectDataAdminMapper collectDataAdminMapper;
    @Autowired
    IDtusAdminService dtusAdminService;
    @Autowired
    IGatewaysAdminService gatewaysAdminService;
    @Autowired
    AlarmInfoAdminMapper alarmInfoAdminMapper;
    @Autowired
    AtDataMapper AtDataMapper;
    @Autowired
    WindSpeedCountMapper windSpeedCountMapper;
    //从前端传过来这次要下入的命令为"AT...//+++"
    public static  String ATcommand="";
    public static void setATcommand(String command)
    {
        ATcommand=command;
    }
    public static Logger log = Logger.getLogger(KafkaReceiver.class);

    public void SetAndReadFrequency(String Message,String type){
        String ACK = Message.substring(12,14);
        String deviceId = Message.substring(14,16);
        AdminCommandInfo commandInfo= new AdminCommandInfo();
        String frameNum = commandInfo.getFrameNum();
        commandInfo = findOne(deviceId,type);
        MessageAckEnum ackResult = MessageAckEnum.findAck(ACK);
        commandInfo.setStatus(ackResult.getAck());
        if("A5".equals(type)&&MessageAckEnum.SuccessMes.getAck().equals(ACK)){
            String time = Message.substring(16,20);
            commandInfo.setContent(ackResult.getDesc()+time);
        }else if("A4".equals(type)){
            commandInfo.setContent("设置成功");
        }else{
            commandInfo.setContent(ackResult.getDesc());
        }
        commandInfo.setReceiveTime(new Date());
        commandInfoAdminService.saveOrUpdate(commandInfo);
    }

    /**
     * 匹配已发送命令
     * @param deviceId,type
     */
    public AdminCommandInfo findOne(String deviceId,String type){
        QueryWrapper<AdminCommandInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(AdminCommandInfo::getType,type)
                    .eq(AdminCommandInfo::getDeviceID,deviceId)
                    .orderByDesc(AdminCommandInfo::getSendTime);
            return commandInfoAdminMapper.selectOne(queryWrapper.last("limit 1"));
    }

    public void performA6(String Message) {
        String sensorId[] = getSensorSettingId(Message);
        String checkNum = A6Utils.getCheckNum(Message);
        Date coltTime = new Date();
        AdminAlarmInfo alarmInfo = new AdminAlarmInfo();
        alarmInfo.setId(A6Utils.getUUID());
        alarmInfo.setAlarmTime(coltTime);
        alarmInfo.setStatus(0);
        alarmInfo.setDeviceId(sensorId[0]+"00");
        alarmInfo.setDeleteState(0);
        alarmInfo.setDeviceId(sensorId[0]);
        if (checkNum.equals("00")||checkNum.equals("02")||A6Utils.getSensorType(Message).equals("湿度传感器")) {
            log.info("设备工作正常");
            String[] sensorsType = InstructionUtil.getSensorType(Message);
            double[] averageSpeed = WindSpeedUtils.windSpeed(Message);
            String[] windDirection = WindSpeedUtils.windDirection(Message);
            String[] windSpeedId = new String[1];//风速的id
            double[] humidity = HumidityUtils.humidityDecode(Message);
            String[] humidityId = new String[3];//湿度的id
            String[] humidityName = {"30cm", "15cm", "5cm"};
            if (sensorsType[0].equals("风速传感器") && A6Utils.getDataLen(Message) > 10) {
                for (int i = 0; i < averageSpeed.length; i++) {
                    windSpeedId[i] = collectDataAdminMapper.selectId(sensorId[0] + "0" + i);
                    //在这里判断是否为空
                    if (windSpeedId[i] == null) {
                        AdminCollectData adminCollectData = new AdminCollectData();
                        adminCollectData.setSensorId(sensorId[0] + "0" + i);
                        adminCollectData.setSensorType(sensorsType[0]);
                        adminCollectData.setSensorValue(String.valueOf(WindSpeedUtils.get10minWindSpeed(Message)) + "m/s");
                        adminCollectData.setSensorParam(windDirection[i]);
                        adminCollectData.setColTime(coltTime);
                        collectDataAdminMapper.insert(adminCollectData);
                    } else {
                        LambdaUpdateWrapper<AdminCollectData> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                        lambdaUpdateWrapper.eq(AdminCollectData::getSensorId, windSpeedId[i])
                                .set(AdminCollectData::getSensorType, sensorsType[0])
                                .set(AdminCollectData::getSensorValue, String.valueOf(WindSpeedUtils.get10minWindSpeed(Message)) + "m/s")
                                .set(AdminCollectData::getSensorParam, windDirection[i])
                                .set(AdminCollectData::getColTime, coltTime);
                        Integer rows = collectDataAdminMapper.update(null, lambdaUpdateWrapper);
                    }
                    influxDBContoller.insertOneToInflux(sensorId[0] + "0" + i, sensorsType[0], WindSpeedUtils.get10minWindSpeed(Message));
                }
            }
            if (sensorsType[0].equals("湿度传感器") && A6Utils.getDataLen(Message) > 10) {
                for (int i = 0; i < humidity.length; i++) {
                    humidityId[i] = collectDataAdminMapper.selectId(sensorId[0] + "0" + i);
                    //这里判断是否为空，为空执行插入
                    if (humidityId[i] == null) {
                        AdminCollectData adminCollectData = new AdminCollectData();
                        adminCollectData.setSensorId(sensorId[0] + "0" + i);
                        adminCollectData.setSensorType(sensorsType[0]);
                        adminCollectData.setSensorValue(String.valueOf(((double) Math.round(humidity[i] * 100) / 100)) + "%H");
                        adminCollectData.setSensorParam(humidityName[i]);
                        adminCollectData.setColTime(coltTime);
                        collectDataAdminMapper.insert(adminCollectData);
                    } else {
                        LambdaUpdateWrapper<AdminCollectData> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                        lambdaUpdateWrapper.eq(AdminCollectData::getSensorId, humidityId[i])
                                .set(AdminCollectData::getSensorType, sensorsType[0])
                                .set(AdminCollectData::getSensorValue, String.valueOf(((double) Math.round(humidity[i] * 100) / 100)) + "%H")
                                .set(AdminCollectData::getSensorParam, humidityName[i])
                                .set(AdminCollectData::getColTime, coltTime);
                        Integer rows = collectDataAdminMapper.update(null, lambdaUpdateWrapper);
                    }
                    influxDBContoller.insertOneToInflux(sensorId[0] + "0" + i, sensorsType[0], (double) Math.round(humidity[i] * 100) / 100);
                }
            }



            //将统计次数插入到wind_speed_count表中
            if (sensorsType[0].equals("风速传感器")){
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                String [] countSpeed = WindSpeedUtils.windSpeedCount(Message);
                WindSpeedCount count = new WindSpeedCount();
                count.setId(uuid);
                count.setSensorData(Message);
                count.setSensorId(sensorId[0]);
                count.setWindSpeedCount0(countSpeed[0]+"次");
                count.setWindSpeedCount1(countSpeed[1]+"次");
                count.setWindSpeedCount2(countSpeed[2]+"次");
                count.setWindSpeedCount3(countSpeed[3]+"次");
                count.setWindSpeedCount4(countSpeed[4]+"次");
                count.setWindSpeedCount5(countSpeed[5]+"次");
                count.setColTime(coltTime);
                windSpeedCountMapper.insert(count);
            }

            //插入到A6_data
            java.sql.Date time = new java.sql.Date(new java.util.Date().getTime());
            String sensorType = A6Utils.getSensorType(Message);
            String averageWindSpeed[] = new String[1];
            String windSpeed10min[] = new String[1];
            String direction[] = new String[1];
            String shidu[] = new String[3];
            for (int i = 0; i < averageWindSpeed.length; i++) {
                //averageWindSpeed[i] = String.valueOf(((double) Math.round(averageSpeed[i]*100)/100))+"m/s";
                windSpeed10min[i] = String.valueOf(WindSpeedUtils.get10minWindSpeed(Message)) + "m/s";
                direction[i] = windDirection[i];
            }
            for (int i = 0; i < shidu.length; i++) {
                shidu[i] = String.valueOf(((double) Math.round(humidity[i] * 100) / 100)) + "%H";
            }
            //插入到mysql数据库表A6_data
            AdminUnresovledData adminUnresovledData = new AdminUnresovledData();
            adminUnresovledData.setData(Message);
            adminUnresovledData.setSensorType(sensorType);
            if (sensorsType[0].equals("风速传感器") && A6Utils.getDataLen(Message) > 10) {
                adminUnresovledData.setSensorData(A6Utils.getSensorData(Message) + "#" + windSpeed10min[0] + direction[0]);
            } else {
                if (sensorsType[0].equals("湿度传感器") && A6Utils.getDataLen(Message) > 10) {
                    adminUnresovledData.setSensorData(InstructionUtil.getSensorData(Message) + "#" + shidu[0] + shidu[1] + shidu[2]);
                }
            }
            adminUnresovledData.setInstructionType(InstructionUtil.getInstructionType(Message));
            adminUnresovledData.setSettingID(sensorId[0]);
            adminUnresovledData.setFrameNum(InstructionUtil.getFrameNum(Message));
            adminUnresovledData.setColTime(time);
            String frameNum = unresovledDataMapper.checkFrameNum(InstructionUtil.getInstructionType(Message));
            String type = unresovledDataMapper.checkSensorType(InstructionUtil.getInstructionType(Message));
            if (frameNum == null) {
                System.out.println("最新的数据为空，执行插入" + InstructionUtil.getFrameNum(Message));
                unresovledDataMapper.insert(adminUnresovledData);
            } else {
                if (frameNum.equals(InstructionUtil.getFrameNum(Message)) && type.equals(sensorsType[0])) {
                    System.out.println("数据重复，舍去");
                } else {
                    System.out.println("执行插入" + InstructionUtil.getFrameNum(Message));
                    unresovledDataMapper.insert(adminUnresovledData);
                }
            }
            deviceStatisticsAdminService.insertDeviceStatistic(Message, sensorId[0]);
            //返回给下位机的命令
            String deviceId = InstructionUtil.getDeviceId(Message);
            /*int crcCheck = CRC16.CRC16_CCITT(new byte[]{(byte) 0xAA, 0x55, (byte) 0xA6, 0x00, 0x02, 0x00, (byte) Integer.parseInt(deviceId)});
            String crcCheckFormat = String.format("%04x", crcCheck);*/
            String FrameNum = Message.substring(4, 6);
            String data="AA55"+FrameNum+"A6000200"+deviceId;
            String crc = HexUtils.hex10To16(CRC16.CRC16_CCITT(HexUtils.hexStringToBytes(data)),4) ;
            String url = "AA55" + FrameNum + "A6" + "0002" + "00" + deviceId + crc + "55AA";
            sendInstrution(url);
        }else {//02只是表明这段时间内数据波动比较大，也可以是正常数据
            /*if (checkNum.equals("02")&&A6Utils.getSensorType(Message).equals("风速传感器")) {
                alarmInfo.setAlarmInfo("数据异常");
                alarmInfo.setAlarmReason("传感器在使用过程中可能部分损坏,此时可能需要现场检修");
                //alarmInfoAdminMapper.insert(alarmInfo);
            } else {*/
                if (checkNum.equals("01")&&A6Utils.getSensorType(Message).equals("风速传感器")){
                    alarmInfo.setAlarmInfo("设备工作状态异常");
                    alarmInfo.setAlarmReason("传感器或者连接线路可能已经损坏，需要实地抢修");
                    alarmInfoAdminMapper.insert(alarmInfo);
                }else {
                    log.info("设备未知错误");
                //}
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
        String settingId;
        String deviceId = InstructionUtil.getDeviceId(Message);
        String change = InstructionUtil.getChange(Message);
        float batteryLevel = InstructionUtil.getBatteryLevel(Message);
        int Percentage = TransferVUtil.encrypt(batteryLevel);
        float batteryPercentage = Percentage / 100.0f;
        System.out.println("电量百分比为" + batteryPercentage);
        influxDBContoller.insertTwoToInfluxDB(deviceId, change, batteryPercentage);
        //插入到MYSQL数据库A6_data
        Date time = new Date();
        AdminUnresovledData dtuData = setData(Message, deviceId, String.valueOf(batteryLevel), "A8",time);
        AdminUnresovledData gatewayData = setData(Message, deviceId, String.valueOf(batteryLevel), "A8",time);
        if(judgeDtuOrGateway(deviceId)==1) {
            settingId = getGatewaySettingId(deviceId);
            gatewayData.setSettingID(settingId);
            updateGateways(settingId,Percentage);
        }
        //UpdateWrapper<AdminDtus> updateWrapper=new UpdateWrapper(conditionBook);
        settingId=getDtuSettingId(deviceId);
        dtuData.setSettingID(settingId);
        updateDtus(settingId,Percentage);
        String frameNum = unresovledDataMapper.checkFrameNum(InstructionUtil.getInstructionType(Message));
        dtuData.setFrameNum(InstructionUtil.getFrameNum(Message));
        gatewayData.setFrameNum(InstructionUtil.getFrameNum(Message));
        if (frameNum == null){
            System.out.println("最新的数据为空，执行插入"+InstructionUtil.getFrameNum(Message));
            if(judgeDtuOrGateway(deviceId)==1)
            unresovledDataMapper.insert(gatewayData);
            unresovledDataMapper.insert(dtuData);
        }else {
            if (frameNum.equals(InstructionUtil.getFrameNum(Message))){
                System.out.println("数据重复，舍去");
            }else {
                System.out.println("执行插入"+InstructionUtil.getFrameNum(Message));
                if(judgeDtuOrGateway(deviceId)==1)
                    unresovledDataMapper.insert(gatewayData);
                unresovledDataMapper.insert(dtuData);
            }
        }
        //插入数据统计中
        deviceStatisticsAdminService.insertDeviceStatistic(Message, settingId);
        //返回给下位机的命令
       /* int crcCheck=CRC16.CRC16_CCITT(new byte[]{(byte) 0xAA,0x55, (byte) 0xA8,0x00,0x02,0x00,(byte) Integer.parseInt(deviceId)});
        String crcCheckFormat = String.format("%04x", crcCheck);*/
        String FrameNum = Message.substring(4, 6);
        String data="AA55"+FrameNum+"A8000200"+deviceId;
        String crc = HexUtils.hex10To16(CRC16.CRC16_CCITT(HexUtils.hexStringToBytes(data)),4) ;
        String url="AA55"+FrameNum+"A8"+"0002"+"00"+deviceId+crc+"55AA";
        sendInstrution(url);
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
    public AdminUnresovledData setData(String message,String deviceId,String BatteryLevel,String InstructionType,Date date)
    {AdminUnresovledData adminUnresovledData = new AdminUnresovledData();
        adminUnresovledData.setData(message);
        adminUnresovledData.setSensorType(deviceId);
        adminUnresovledData.setSensorData(BatteryLevel);
        adminUnresovledData.setInstructionType(InstructionType);
        adminUnresovledData.setColTime(date);
        return adminUnresovledData;
    }
    /**
     * 判断是网关还是DTU，为网关则返回1
     * @param deviceId
     * @return
     */
    public int judgeDtuOrGateway(String deviceId)
    {if (deviceId.equals("01")||deviceId.equals("FF"))//增加ff作为第二个网关地址
        return 1;
    return 0;
    }
    public void updateDtus(String settingId,float Percentage )
    {
        AdminDtus Dtu=new AdminDtus();
        Dtu.setDtuId(settingId);
        UpdateWrapper<AdminDtus> updateWrapper=new UpdateWrapper(Dtu);
        updateWrapper.set("elec_charge",String.valueOf((int)Percentage));
        dtusAdminService.update(updateWrapper);
    }
    public void updateGateways(String settingId,float Percentage)
    {
        AdminGateways gateway=new AdminGateways();
        gateway.setGateId(settingId);
        UpdateWrapper<AdminGateways> updateWrapper=new UpdateWrapper(gateway);
        updateWrapper.set("elec_charge",String.valueOf((int)Percentage));
        gatewaysAdminService.update(updateWrapper);
    }
    /**
     * 获取DTU或者Gateway的SettingId
     *
     * @param deviceId
     * @return
     */
    public String getGatewaySettingId(String deviceId) {
            return "GW" + deviceId;
    }
    public String getDtuSettingId(String deviceId)
    {
        String settingId = "DTU" + deviceId;
        String gatewaySettingId = gatewayDtuAdminMapper.selectGatewaySettingId(settingId);
        return gatewaySettingId + settingId;
    }
    /**
     * 获取传感器的SettingId
     *
     * @param message
     * @return
     */
    public  String[] getSensorSettingId(String message) {
        String deviceId = InstructionUtil.getDeviceId(message);
        String dtuSettingId = getDtuSettingId(deviceId);
        String[] sensorType = InstructionUtil.getSensorType(message);
        String[] sensorAddress = InstructionUtil.getSensorAddress(message);
        String[] sensorSettingId = new String[sensorType.length];
        for (int i = 0; i < sensorType.length; i++) {
            if (sensorType[i].equals("湿度传感器"))
                sensorSettingId[i] = "SD" + dtuSettingId + "01" + sensorAddress[i];
            if (sensorType[i].equals("风速传感器"))
                sensorSettingId[i] = "FS" + dtuSettingId + "02" + sensorAddress[i];
            if (sensorType[i].equals("水盐传感器"))
                sensorSettingId[i] = "WS" + dtuSettingId + "03" + sensorAddress[i];
        }
        return sensorSettingId;
    }

    public static  void sendInstrution(String url) {


        RestUtil restUtil = new RestUtil();


        try {
            String resultString = restUtil.load(

                    "http://39.105.171.192:8886/command?cmd="+url);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //执行网关配置命令
    public  void performCommand(String message) {

        AdminAtData adminAtData = new AdminAtData();
        adminAtData.setAt(ATcommand);
        Date date = new Date();
        adminAtData.setCreatedTime(date);
        adminAtData.setData(message);
        AtDataMapper.insert(adminAtData);
        System.out.println("插入成功");
    }
    public static void main(String[] args) {
       /* PerformInstrution performInstrution = new PerformInstrution();
        String[] settingId = performInstrution.getSensorSettingId("AA552AA6001F020200011A004A0000006A016400510150003400E1002B00CB002500C100129DA655AA");
        for (int i = 0; i <settingId.length ; i++) {
            System.out.println(settingId[i]);
        }*/
        //AA55F9A60028010200010F00140000001600420014004B0001A0
        String test = "AA5573A6001F010200011A005500090033014D003301520047001E0039016300360000010D6C9C55AA0D0AAA5573A6001F010200011A005500090033014D003301520047001E0039016300360000010D6C9C55AA0D0A";//AA5573A6001F010200011A005500090033014D003301520047001E0039016300360000010D6C9C55AA0D0AAA5573A6001F010200011A005500090033014D003301520047001E0039016300360000010D6C9C55AA0D0A
        PerformInstrution performInstrution = new PerformInstrution();
        performInstrution.performA6(test);
    }
}
