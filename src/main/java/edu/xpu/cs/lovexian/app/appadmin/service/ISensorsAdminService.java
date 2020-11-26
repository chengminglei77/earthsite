package edu.xpu.cs.lovexian.app.appadmin.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtuSensor;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;

/**
 *  Service接口
 *
 * @author xpu
 * @date 2020-09-01 22:02:59
 */
@DS("slave")
public interface ISensorsAdminService extends IService<AdminSensors> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param adminSensors adminSensors
     * @return IPage<AdminSensors>
     */
    IPage<AdminSensors> findSensorss(QueryRequest request, AdminSensors adminSensors);

    /*
    通过id删除sensors信息
     */
    boolean deleteSensors(String id);
    //模糊查询，返回页面
    IPage<AdminSensors> querySensorsInfo(QueryRequest request,AdminSensors adminSensors);
    //查找sensors状态为0的值
    IPage<AdminSensors> findSensorsByTypeId(QueryRequest request,AdminSensors adminSensors);
    //查找与对应sensors相连的dtu信息
    IPage<AdminDtuSensor> findSensorsDtuInfo(QueryRequest request,AdminSensors adminSensors);
    boolean completelyDeleteSensors(String id);

    public boolean restoreSensors(String id);

    boolean restoreById(String id);

    //查询传感器数据的数据终端设备地址
    String querySensorAdress(String message);

}
