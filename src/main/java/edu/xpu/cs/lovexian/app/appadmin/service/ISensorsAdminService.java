package edu.xpu.cs.lovexian.app.appadmin.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;

import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
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
    IPage<AdminSensors> querySensorsInfo(QueryRequest request,AdminSensors adminSensors);
    IPage<AdminSensors> findSensorsByTypeId(QueryRequest request,AdminSensors adminSensors);
}
