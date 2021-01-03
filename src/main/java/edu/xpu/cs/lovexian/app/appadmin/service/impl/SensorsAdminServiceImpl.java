package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtuSensor;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;
import edu.xpu.cs.lovexian.app.appadmin.mapper.SensorsAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.ISensorsAdminService;
import edu.xpu.cs.lovexian.app.appadmin.utils.StatusEnum;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

/**
 * Service实现
 *
 * @author xpu
 * @date 2020-09-01 22:02:59
 */
@Service("sensorsAdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SensorsAdminServiceImpl extends ServiceImpl<SensorsAdminMapper, AdminSensors> implements ISensorsAdminService {

    @Autowired
    private SensorsAdminMapper sensorsAdminMapper;

    @Override
    public IPage<AdminSensors> findSensorss(QueryRequest request, AdminSensors adminSensors) {
        QueryWrapper<AdminSensors> queryWrapper = new QueryWrapper<>();
        //按照所获取的getpagenum
        Page<AdminSensors> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    /*
    要删除正常状态的Sensors其实是将它的状态置为1，还没有真正的从数据库中删除
    通过ID删除对应的Sensors
     */
    @Override
    public boolean deleteSensors(String id) {
        //新建条件构造器，用来修改Sensors的状态值
        UpdateWrapper<AdminSensors> updateWrapper = new UpdateWrapper<>();
        //通过条件构造器寻找与给定id值相同的Sensors，将它的状态置为1
        updateWrapper.lambda().eq(AdminSensors::getId, id).set(AdminSensors::getDeleteState, 1);
        //删除数据
        return this.update(updateWrapper);
    }

    /*
       查询相应的信息
     */
    @Override
    public IPage<AdminSensors> querySensorsInfo(QueryRequest request, AdminSensors adminSensors) {
        //新建查询条件器
        QueryWrapper<AdminSensors> queryWrapper = new QueryWrapper<>();
        //如果Sensors存在，即id不为空
        if (adminSensors.getSensorId() != null) {
            //寻找与所传的Sensors对象相同的Sensors
            queryWrapper.lambda().eq(AdminSensors::getSensorId, adminSensors.getSensorId());
        }
        Page<AdminSensors> adminSensorsPage = new Page<>(request.getPageNum(), request.getPageSize());
        //按request的页数，每页的大小生成对应的IPage<Sensors>的对象
        return this.page(adminSensorsPage, queryWrapper);
    }

    @Override
    public IPage<AdminSensors> findSensorsByTypeId(QueryRequest request, AdminSensors adminSensors) {
        QueryWrapper<AdminSensors> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(adminSensors.getSensorId())) {
            queryWrapper.lambda().like(AdminSensors::getSensorId, adminSensors.getSensorId()).orderByDesc(AdminSensors::getCreatedAt);
        }
        //如果sensors的TypeId值不为空
        if (StringUtils.isNotBlank(adminSensors.getTypeId())) {
            queryWrapper.lambda().like(AdminSensors::getTypeId, adminSensors.getTypeId()).orderByDesc(AdminSensors::getCreatedAt);
        }

        if (adminSensors.getStatus() != null) {
            queryWrapper.lambda().eq(AdminSensors::getStatus, adminSensors.getStatus()).orderByDesc(AdminSensors::getCreatedAt);
        }/*else{
            adminSensors.setStatus(StatusEnum.NORMAL_STATE.getCode());//0为未删除状态
            System.out.println("查询为数据的标志state=="+adminSensors.getStatus());
            queryWrapper.lambda().eq(AdminSensors::getStatus,adminSensors.getStatus());
        }*/

        if (adminSensors.getDeleteState() != null) {
            queryWrapper.lambda().eq(AdminSensors::getDeleteState, adminSensors.getDeleteState());
        } else {
            adminSensors.setDeleteState(StatusEnum.NORMAL_STATE.getCode());//0为未删除状态
            System.out.println("查询为删除数据的标志Delstate==" + adminSensors.getDeleteState());
            queryWrapper.lambda().eq(AdminSensors::getDeleteState, adminSensors.getDeleteState()).orderByDesc(AdminSensors::getCreatedAt);
        }

        //排除某些字段
        Page<AdminSensors> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public IPage<AdminDtuSensor> findSensorsDtuInfo(QueryRequest request, AdminSensors adminSensors) {
        return null;
    }

    @Override
    public boolean completelyDeleteSensors(String id) {
        sensorsAdminMapper.deleteById(id);
        return true;
    }

    @Override
    public boolean restoreSensors(String id) {
        return false;
    }

    @Override
    public boolean restoreById(String id) {
        UpdateWrapper<AdminSensors> updateWrapper = new UpdateWrapper<>();
        //还原逻辑删除的报警信息
        updateWrapper.lambda().eq(AdminSensors::getId, id).set(AdminSensors::getDeleteState, 0);
        return this.update(updateWrapper);
    }

    //查询传感器数据(地址)指令
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
                    String s1 = message.substring(14, 16);
                    return s1;
                } else return "null";
            }
            if (h.equals("A8")) {
                String s = message.substring(12, 14);
                String s1 = message.substring(14, 18);
                String sub = new BigInteger(s1, 16).toString(10);
                return s + sub;
            }
            if (h.equals("A9")) {
                String s = message.substring(12, 14);
                if (s.equals("01"))
                    return "失败";
                if (s.equals("02"))
                    return "CRC校验失败";
                if (s.equals("00")) {

                    String s1 = message.substring(14, 16);
                    String s2 = message.substring(16, 20);
                    String sub = new BigInteger(s2, 16).toString(10);
                    return s1 + sub;
                } else return "null";
            }

            if (h.equals("A4")) {
                String s = message.substring(12, 18);
                return s;
            }
            /**
             * 获取传感器上报数据时间
             */
            if (h.equals("A5")) {
                String CRC = message.substring(12, 14);
                if (CRC.equals("00")) {
                    String s = message.substring(14, 20);
                    return s;
                    //return "成功";
                }
                if (CRC.equals("01")) {
                    return "失败";
                }
                if (CRC.equals("02")) {
                    return "CRC校验失败";
                }
            }
            /**
             *上报传感器数据指令
             */
            if (h.equals("A6")) {
                String s = message.substring(12, 24);
                return s;
            }
            /**
             *上报数据的传感器类型和传感器地址
             */
           /* if (h.equals("A1")){
                  String Device_ID = message.substring(12, 14);
                  String Sensor_Serial_Num = message.substring(14, 16);
                  String Sensor_Type = message.substring(16, 18);
                  String Sensor_Addr = message.substring(18, 22);
                  return Device_ID;
            }*/

            else return "null";
        } else return "null";
    }
}
