package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensorsData;
import edu.xpu.cs.lovexian.app.appadmin.mapper.SensorsDataAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.ISensorsDataAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

/**
 * @author czy
 * @create 2020-11-27-10:20
 */
@Service("sensorsDataAdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SensorsDataAdminServiceImpl extends ServiceImpl<SensorsDataAdminMapper, AdminSensorsData> implements ISensorsDataAdminService {
    @Autowired
    private SensorsDataAdminMapper sensorsDataAdminMapper;

    @Override
    public String setSensorAddrAndType(String message){
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            String h = message.substring(6, 8);
            if (h.equals("A1")){
                String ACK = message.substring(12, 14);
                if (ACK.equals("01"))
                    return "失败";
                if (ACK.equals("02"))
                    return "CRC校验失败";
                if(ACK.equals("00")) {
                    AdminSensorsData data = new AdminSensorsData();
                    String Device_ID = message.substring(14, 16);
                    data.setDeviceId(Device_ID);
                    sensorsDataAdminMapper.insert(data);
                    return "成功";
                }else return "错误";
            }else return "错误";
        }else return "错误";
    }
    @Override
    public String reportSensorAddrAndTypeAndNum(String message){
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            String h = message.substring(6, 8);
            if (h.equals("A2")){
                String ACK = message.substring(12, 14);
                if (ACK.equals("01"))
                    return "失败";
                if (ACK.equals("02"))
                    return "CRC校验失败";
                if(ACK.equals("00")) {
                    AdminSensorsData data = new AdminSensorsData();
                    String Device_ID = message.substring(14, 16);
                    data.setDeviceId(Device_ID);
                    String Sensor_Num = message.substring(16, 18);
                    Integer Num = Integer.valueOf(Sensor_Num, 16);
                    //System.out.println(Num);
                    data.setSensorNum(Num);
                    for (int N=0;N<Num;N++) {
                        String Sensor_Type = message.substring(18+N*6, 20+N*6);
                        Integer Typ = Integer.valueOf(Sensor_Type, 16);
                        data.setSensorType(Typ);
                        String Sensor_Addr = message.substring(20+N*6, 24+N*6);
                        data.setSensorAddr(Sensor_Addr);
                    }
                    sensorsDataAdminMapper.insert(data);
                    return "成功";
                }else return "错误";
            }else return "错误";
        }else return "错误";
    }
    @Override
    public String deleteSensor(String message){
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            String h = message.substring(6, 8);
            if (h.equals("A3")){
                String ACK = message.substring(12, 14);
                if (ACK.equals("01"))
                    return "失败";
                if (ACK.equals("02"))
                    return "CRC校验失败";
                if(ACK.equals("00")) {
                    AdminSensorsData data = new AdminSensorsData();
                    String Device_ID = message.substring(14, 16);
                    data.setDeviceId(Device_ID);
                    sensorsDataAdminMapper.insert(data);
                    return "成功";
                }else return "错误";
            }else return "错误";
        }else return "错误";
    }
    @Override
    public String querySensorAdress(String message) {
        //String message="AA550A0700010255AA";
        //StringBuilder sb = new StringBuilder();
        int i;
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            String h = message.substring(6, 8);
            if (h.equals("A7")) {
                String s = message.substring(12, 14);
                if (s.equals("01"))
                    return "失败";
                if (s.equals("02"))
                    return "CRC校验失败";
                if (s.equals("00")) {
                    AdminSensorsData data = new AdminSensorsData();
                    data.setDeviceId(s);
                    sensorsDataAdminMapper.insert(data);
                    return "成功";
                    //bookList.add(book);
                } else return "null";
            }
            if (h.equals("A8")) {
                AdminSensorsData data = new AdminSensorsData();
                String s = message.substring(12, 14);
                data.setDeviceId(s);
                String s1 = message.substring(14, 18);
                String sub = new BigInteger(s1, 16).toString(10);
                data.setBatteryLevel(sub);
                sensorsDataAdminMapper.insert(data);
                return "成功";
            }
            if (h.equals("A9")) {
                String s = message.substring(12, 14);
                if (s.equals("01"))
                    return "失败";
                if (s.equals("02"))
                    return "CRC校验失败";
                if (s.equals("00")) {
                    AdminSensorsData data = new AdminSensorsData();
                    String s1 = message.substring(14, 16);
                    data.setDeviceId(s1);
                    String s2 = message.substring(16, 20);
                    String sub = new BigInteger(s2, 16).toString(10);
                    data.setBatteryLevel(sub);
                    sensorsDataAdminMapper.insert(data);
                    return "成功";
                } else return "错误";
            } else return "错误";
        }else return "错误";
    }

    public static void main(String[] args) {
        SensorsDataAdminServiceImpl se = new SensorsDataAdminServiceImpl();
        se.querySensorAdress("AA5500A2000600AA0201AACC55AA");
    }
}