package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtus;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensorsData;
import edu.xpu.cs.lovexian.app.appadmin.mapper.DtusAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.SensorsAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.mapper.SensorsDataAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IDtusAdminService;
import edu.xpu.cs.lovexian.app.appadmin.service.ISensorsAdminService;
import edu.xpu.cs.lovexian.app.appadmin.service.ISensorsDataAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author czy
 * @create 2020-11-27-10:20
 */
@Service("sensorsDataAdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SensorsDataAdminServiceImpl extends ServiceImpl<SensorsDataAdminMapper, AdminSensorsData> implements ISensorsDataAdminService {
    @Autowired
    private SensorsDataAdminMapper sensorsDataAdminMapper;
    @Autowired
    private DtusAdminMapper dtusAdminMapper;
    @Autowired
    private IDtusAdminService dtusAdminService;

    @Override
    public void querySensorAdress(String message) throws Exception{
        //String message="AA550A0700010255AA";
        //StringBuilder sb = new StringBuilder();
        int i;
        if (message.startsWith("AA55") && message.endsWith("55AA")) {
            String h = message.substring(6, 8);
            if (h.equals("A7")) {
                String s = message.substring(12, 14);
                String deviceId = message.substring(14, 16);
                if (s.equals("01"))
                    throw new Exception("失败");
                    //System.out.println("失败");
                if (s.equals("02"))
                    throw new Exception("CRC校验失败");
                    //System.out.println(" CRC校验失败");
                if (s.equals("00"))
                {
                    AdminDtus adminDtus=new AdminDtus();
                    adminDtus.setDtuAddress(deviceId);
                    System.out.println("数据终端设备地址为"+deviceId);
            }
            }
            if (h.equals("A8")) {
                String s = message.substring(12, 14);
                String deviceId = message.substring(14, 16);
                if (s.equals("01"))
                    throw new Exception("失败");
                    //System.out.println("失败");
                if (s.equals("02"))
                    throw new Exception("CRC校验失败");
                   // System.out.println(" CRC校验失败");
                if (s.equals("00"))
                    System.out.println("数据终端设备地址为"+deviceId);
            }
            if (h.equals("A9")) {
                String s = message.substring(12, 14);
                if (s.equals("01"))
                    throw new Exception("失败");
                    //System.out.println("失败");
                if (s.equals("02"))
                    throw new Exception("CRC校验失败");
                //System.out.println(" CRC校验失败");
                if (s.equals("00")) {
                    //数据终端设备地址
                    String s1 = message.substring(14, 16);
                    //电池电量
                    String s2 = message.substring(16, 20);
                    String sub = new BigInteger(s2, 16).toString(10);
                    AdminDtus adminDtus=new AdminDtus();
                    adminDtus.setDtuAddress(s1);
                    UpdateWrapper<AdminDtus> updateWrapper=new UpdateWrapper(adminDtus);
                    updateWrapper.set("elec_charge",sub);
                }
            }
        }
    }
}