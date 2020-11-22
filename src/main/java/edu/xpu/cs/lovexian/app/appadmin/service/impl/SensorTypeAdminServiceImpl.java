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
    public boolean deleteSensorsTypeByid(String id) {
        UpdateWrapper<AdminSensorType> updateWrapper = new UpdateWrapper<>();
        //通过条件构造器寻找与给定id值相同的Sensors，将它的状态置为1
        updateWrapper.lambda().eq(AdminSensorType::getId,id).set(AdminSensorType::getDeleteState,1);
        //删除数据
        return this.update(updateWrapper);
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
        if(adminSensorType.getDeleteState()!=null)
        {
            queryWrapper.lambda().like(AdminSensorType::getDeleteState,adminSensorType.getDeleteState());
        }
        else {
            adminSensorType.setDeleteState(StatusEnum.NORMAL_STATE.getCode());//0为未删除状态
            System.out.println("查询为删除数据的标志state==" + adminSensorType.getDeleteState());
            queryWrapper.lambda().eq(AdminSensorType::getDeleteState, adminSensorType.getDeleteState());
        }

        if(StringUtils.isNotBlank(adminSensorType.getSensorName()))
        {
            queryWrapper.lambda().like(AdminSensorType::getSensorName,adminSensorType.getSensorName());
        }
         if(StringUtils.isNotBlank(adminSensorType.getSensorModel()))
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

    @Override
    public boolean restoreSensors(String id) {
        UpdateWrapper<AdminSensorType> updateWrapper = new UpdateWrapper<>();
        //还原逻辑删除的报警信息

        updateWrapper.lambda().eq(AdminSensorType::getId, id).set(AdminSensorType::getDeleteState, 0);
        return this.update(updateWrapper);
    }
}
