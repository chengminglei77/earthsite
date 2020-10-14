package edu.xpu.cs.lovexian.app.appadmin.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtuSensor;

import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *  Service接口
 *
 * @author xpu
 * @date 2020-09-21 16:34:55
 */
@DS("slave")
public interface IDtuSensorAdminService extends IService<AdminDtuSensor> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param adminDtuSensor adminDtuSensor
     * @return IPage<AdminDtuSensor>
     */
    IPage<AdminDtuSensor> findDtuSensors(QueryRequest request, AdminDtuSensor adminDtuSensor);


    IPage<AdminDtuSensor> findSensorsByTypeId(QueryRequest request, AdminDtuSensor adminDtuSensor);

    IPage<AdminDtuSensor> findAgentChecks(QueryRequest request, AdminDtuSensor adminDtuSensor);
}
