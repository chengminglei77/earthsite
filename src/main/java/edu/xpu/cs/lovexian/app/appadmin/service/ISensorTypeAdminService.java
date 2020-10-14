package edu.xpu.cs.lovexian.app.appadmin.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensorType;

import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensorType;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author xpu
 * @date 2020-09-27 19:33:26
 */
@DS("slave")
public interface ISensorTypeAdminService extends IService<AdminSensorType> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param adminSensorType adminSensorType
     * @return IPage<AdminSensorType>
     */
    IPage<AdminSensorType> findSensorTypes(QueryRequest request, AdminSensorType adminSensorType);
    boolean deleteSensorsTypeByid(String id);
    boolean deleteSensorsType(String id);
    IPage<AdminSensorType> findSensorsTypeByTypeId(QueryRequest request, AdminSensorType adminSensorType);
    IPage<AdminSensorType> querySensorsTypeInfo(QueryRequest request,AdminSensorType adminSensorType);

    boolean restoreSensors(String id);
}
