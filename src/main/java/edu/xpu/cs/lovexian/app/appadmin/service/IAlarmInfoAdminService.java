package edu.xpu.cs.lovexian.app.appadmin.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminAlarmInfo;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;

/**
 *  Service接口
 *
 * @author xpu
 * @date 2020-09-21 19:42:21
 */
@DS("slave")
public interface IAlarmInfoAdminService extends IService<AdminAlarmInfo> {

    /**
     * 查找不同类型的信息
     * @param request
     * @param adminAlarmInfo
     * @return
     */

    IPage<AdminAlarmInfo> findAlarmInfos(QueryRequest request, AdminAlarmInfo adminAlarmInfo);


    boolean deleteAlarms(String id);//跳转到deleteById，BatchDelete/{actionIds

    IPage<AdminAlarmInfo> queryAlarmInfos(QueryRequest request, AdminAlarmInfo adminAlarmInfo);

    public IPage<AdminAlarmInfo> findAlarmInfosByTypeId(QueryRequest request, AdminAlarmInfo adminAlarmInfo);

    public boolean completelyDeletealarmInfo(String id);

    public boolean restoreAlarms(String id);
}