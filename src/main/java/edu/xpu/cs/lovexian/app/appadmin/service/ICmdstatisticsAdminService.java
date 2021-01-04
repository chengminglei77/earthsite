package edu.xpu.cs.lovexian.app.appadmin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminUnresovledData;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;

public interface ICmdstatisticsAdminService extends IService<AdminUnresovledData> {

    IPage<AdminUnresovledData> findCmds(QueryRequest request, AdminUnresovledData adminAlarmInfo);

    IPage<AdminUnresovledData> findCmdsByTypeId(QueryRequest request, AdminUnresovledData adminAlarmInfo);

}
