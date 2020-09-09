package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtus;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGateways;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminLawerInfo;
import edu.xpu.cs.lovexian.app.appadmin.mapper.GatewaysAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IGatewaysAdminService;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 *  Service实现
 *
 * @author xpu
 * @date 2020-09-01 17:01:18
 */
@Service("GatewaysAdminService")
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true,rollbackFor = Exception.class)
public class GatewaysAdminServiceImpl extends ServiceImpl<GatewaysAdminMapper, AdminGateways> implements IGatewaysAdminService {
    @Autowired
    private GatewaysAdminMapper gatewaysAdminMapper;

    @Override
    public IPage<AdminGateways> findGatewayss(QueryRequest request, AdminGateways adminGateways) {
        QueryWrapper<AdminGateways> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        Page<AdminGateways> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public IPage<AdminGateways> findGatewaysByTypeId(QueryRequest request, AdminGateways adminGateways) {
        QueryWrapper<AdminGateways> queryWrapper = new QueryWrapper<>();
        //模糊查询
        if (StringUtils.isNotBlank(adminGateways.getServerPort())) {
            queryWrapper.lambda().like(AdminGateways::getServerPort, adminGateways.getServerPort());
        }
        if (adminGateways.getStatus() != null) {
            //相当于where status=....
            queryWrapper.lambda().eq(AdminGateways::getStatus, adminGateways.getStatus());
        } else {
            adminGateways.setStatus(0);//0为未删除状态
            System.out.println("查询为删除数据的标志state==" + adminGateways.getStatus());
            queryWrapper.lambda().eq(AdminGateways::getStatus, adminGateways.getStatus());
        }

        Page<AdminGateways> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean deleteGateWays(String id) {
        UpdateWrapper<AdminGateways> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(AdminGateways::getId,id).set(AdminGateways::getStatus,1);
        return this.update(updateWrapper);

    }

    @Override
    public IPage<AdminGateways> queryGateways(QueryRequest request, AdminGateways adminGateways) {
        QueryWrapper<AdminGateways> queryWrapper = new QueryWrapper<>();
        //查询对应字段lawInfo
        if (adminGateways.getServerPort() != null) {
            queryWrapper.lambda().eq(AdminGateways::getServerPort, adminGateways.getServerPort());
        }
        Page<AdminGateways> adminGatewaysPage = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(adminGatewaysPage, queryWrapper);
    }


}
