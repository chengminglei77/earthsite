package edu.xpu.cs.lovexian.app.appadmin.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminAlarmInfo;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtus;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGatewayDtu;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;

import java.util.List;

/**
 * Service接口
 *
 * @author xpu
 * @date 2020-09-21 19:42:21
 */
@DS("slave")
public interface IAlarmInfoAdminService extends IService<AdminAlarmInfo> {

    /**
     * 查找不同类型的信息
     *
     * @param request
     * @param adminAlarmInfo
     * @return
     */

    IPage<AdminAlarmInfo> findAlarmInfos(QueryRequest request, AdminAlarmInfo adminAlarmInfo);

    boolean deleteAlarms(String id);//跳转到deleteById，BatchDelete/{actionIds

    IPage<AdminAlarmInfo> queryAlarmInfos(QueryRequest request, AdminAlarmInfo adminAlarmInfo);

    //查询网关历史报警信息
    IPage<AdminAlarmInfo> gatewayAlarmInfoHistory(QueryRequest request, String gateId);

    //查询传感器历史报警信息
    List<AdminAlarmInfo> sensorsAlarmInfoHistory(String sensorId);
    //IPage<AdminSensors> sensorsAlarmInfoHistory(QueryRequest request, String sensorId);

    //查询dtu历史报警信息
    IPage<AdminAlarmInfo> dtuAlarmInfoHistory(QueryRequest request, String dtuId);

    public IPage<AdminAlarmInfo> findAlarmInfosByTypeId(QueryRequest request, AdminAlarmInfo adminAlarmInfo);

    public boolean completelyDeletealarmInfo(String id);


    public boolean restoreAlarms(String id);
}
