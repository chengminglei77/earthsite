package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGateways;
import edu.xpu.cs.lovexian.app.appadmin.mapper.GatewaysAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IGatewaysAdminService;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
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
@Service
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true,rollbackFor = Exception.class)
public class GatewaysAdminServiceImpl extends ServiceImpl<GatewaysAdminMapper, AdminGateways> implements IGatewaysAdminService {
    @Autowired
    private GatewaysAdminMapper gatewaysAdminMapper;

    @Override
    public IPage<AdminGateways> findGatewayss(QueryRequest request,AdminGateways adminGateways){
        QueryWrapper<AdminGateways> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        Page<AdminGateways> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page,queryWrapper);
    }
}
