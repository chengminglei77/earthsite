package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensorType;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;
import edu.xpu.cs.lovexian.app.appadmin.mapper.SensorTypeAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.ISensorTypeAdminService;
import edu.xpu.cs.lovexian.app.appadmin.utils.StatusEnum;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 *  Service实现
 *
 * @author xpu
 * @date 2020-09-27 19:33:26
 */
@Service("sensorTypeAdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SensorTypeAdminServiceImpl extends ServiceImpl<SensorTypeAdminMapper, AdminSensorType> implements ISensorTypeAdminService {

    @Autowired
    private SensorTypeAdminMapper sensorTypeAdminMapper;

    @Override
    public IPage<AdminSensorType> findSensorTypes(QueryRequest request, AdminSensorType adminSensorType) {
        QueryWrapper<AdminSensorType> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        Page<AdminSensorType> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean deleteSensorsType(String id) {
        sensorTypeAdminMapper.deleteById(id);
        return true;
    }

    //查询所有的传感器类型
    @Override
    public IPage<AdminSensorType> findSensorsTypeByTypeId(QueryRequest request, AdminSensorType adminSensorType) {
        QueryWrapper<AdminSensorType> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(adminSensorType.getSensorName()))
        {
            queryWrapper.lambda().like(AdminSensorType::getSensorName,adminSensorType.getSensorName());
        }
        else if(StringUtils.isNotBlank(adminSensorType.getSensorModel()))
        {
            queryWrapper.lambda().like(AdminSensorType::getSensorModel,adminSensorType.getSensorModel());
        }
        Page<AdminSensorType> page = new Page<>(request.getPageNum(), request.getPageSize());
        return  this.page(page,queryWrapper);
    }

    @Override
    public IPage<AdminSensorType> querySensorsTypeInfo(QueryRequest request, AdminSensorType adminSensorType) {
        QueryWrapper<AdminSensorType> QueryWrapper = new QueryWrapper<>();
        if(adminSensorType.getSensorName()!=null)
        {
            QueryWrapper.lambda().eq(AdminSensorType::getSensorName,adminSensorType.getSensorName());
        }
        Page<AdminSensorType> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page,QueryWrapper);
    }
}
