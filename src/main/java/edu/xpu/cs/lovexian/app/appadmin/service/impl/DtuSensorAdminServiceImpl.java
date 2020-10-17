package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtuSensor;
import edu.xpu.cs.lovexian.app.appadmin.mapper.DtuSensorAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IDtuSensorAdminService;
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
 * @date 2020-09-21 16:34:55
 */
@Service("dtuSensorAdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DtuSensorAdminServiceImpl extends ServiceImpl<DtuSensorAdminMapper, AdminDtuSensor> implements IDtuSensorAdminService {

    @Autowired
    private DtuSensorAdminMapper dtuSensorAdminMapper;

    @Override
    public IPage<AdminDtuSensor> findDtuSensors(QueryRequest request, AdminDtuSensor adminDtuSensor) {
        QueryWrapper<AdminDtuSensor> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        Page<AdminDtuSensor> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public IPage<AdminDtuSensor> findSensorsByTypeId(QueryRequest request, AdminDtuSensor adminDtuSensor) {
        QueryWrapper<AdminDtuSensor> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(adminDtuSensor.getSensorId()))
        {
            queryWrapper.lambda().like(AdminDtuSensor::getSensorId,adminDtuSensor.getSensorId());
        }
        if(StringUtils.isNotBlank(adminDtuSensor.getDtuId())){
            queryWrapper.lambda().like(AdminDtuSensor::getDtuId,adminDtuSensor.getDtuId());
        }
        Page<AdminDtuSensor> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public IPage<AdminDtuSensor> findAgentChecks(QueryRequest request, String dtuId) {
        Page<AdminDtuSensor> page = new Page<>(request.getPageNum(), request.getPageSize());
        IPage<AdminDtuSensor> result=dtuSensorAdminMapper.selectCheckInfos(page,dtuId);
        System.out.println(result);
        return dtuSensorAdminMapper.selectCheckInfos(page,dtuId);
    }


}
