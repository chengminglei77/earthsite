package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;
import edu.xpu.cs.lovexian.app.appadmin.mapper.SensorsAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.ISensorsAdminService;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *  Service实现
 *
 * @author xpu
 * @date 2020-09-01 22:02:59
 */
@Service("sensorsAdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SensorsAdminServiceImpl extends ServiceImpl<SensorsAdminMapper, AdminSensors> implements ISensorsAdminService {

    @Autowired
    private SensorsAdminMapper sensorsAdminMapper;

    @Override
    public IPage<AdminSensors> findSensorss(QueryRequest request, AdminSensors adminSensors) {
        QueryWrapper<AdminSensors> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        Page<AdminSensors> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }
    /*
    将AdminSensors的状态置为1
     */
    @Override
    public boolean deleteSensors(String id) {
        UpdateWrapper<AdminSensors> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(AdminSensors::getId,id).set(AdminSensors::getStatus,1);
        return this.update(updateWrapper);
    }
/*
   查询相应的信息
 */
    @Override
    public IPage<AdminSensors> querySensorsInfo(QueryRequest request, AdminSensors adminSensors) {
        QueryWrapper<AdminSensors> queryWrapper = new QueryWrapper<>();
        if (adminSensors.getSensorId()!=null){
            queryWrapper.lambda().eq(AdminSensors::getSensorId,adminSensors.getSensorId());
        }
        Page<AdminSensors> adminSensorsPage = new Page<>(request.getPageNum(),request.getPageSize());
        return this.page(adminSensorsPage,queryWrapper);
    }

    @Override
    public IPage<AdminSensors> findSensorsByTypeId(QueryRequest request, AdminSensors adminSensors) {
        QueryWrapper<AdminSensors> queryWrapper = new QueryWrapper<>();

        //如果sensors的TypeId值不为空
        if(StringUtils.isNotBlank(adminSensors.getTypeId())){
            queryWrapper.lambda().like(AdminSensors::getTypeId,adminSensors.getTypeId());
        }
        if(adminSensors.getStatus()!=null){
            queryWrapper.lambda().eq(AdminSensors::getStatus,adminSensors.getStatus());
        }else{
            adminSensors.setStatus(0);//0为未删除状态
            System.out.println("查询为删除数据的标志state=="+adminSensors.getStatus());
            queryWrapper.lambda().eq(AdminSensors::getStatus,adminSensors.getStatus());
        }

        //排除某些字段
        Page<AdminSensors> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }
}
