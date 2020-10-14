package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGatewayDtu;
import edu.xpu.cs.lovexian.app.appadmin.mapper.GatewayDtuAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IGatewayDtuAdminService;
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
 * @date 2020-09-01 21:51:18
 */
@Service("gatewayDtuAdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class GatewayDtuAdminServiceImpl extends ServiceImpl<GatewayDtuAdminMapper,AdminGatewayDtu> implements IGatewayDtuAdminService {

    @Autowired
    private GatewayDtuAdminMapper gatewayDtuAdminMapper;

    @Override
    public IPage<AdminGatewayDtu> findGatewayDtus(QueryRequest request, AdminGatewayDtu adminGatewayDtu) {
        QueryWrapper<AdminGatewayDtu> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        Page<AdminGatewayDtu> page = new Page<>(request.getPageNum(),request.getPageSize());
        return this.page(page,queryWrapper);
    }

    @Override
    public IPage<AdminGatewayDtu> findGatewayDtuByTypeId(QueryRequest request, AdminGatewayDtu adminGatewayDtu) {
        QueryWrapper<AdminGatewayDtu> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(adminGatewayDtu.getGatewayId())) {
            queryWrapper.lambda().like(AdminGatewayDtu::getGatewayId, adminGatewayDtu.getGatewayId());
        }


        Page<AdminGatewayDtu> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean updateGatewayDtu(AdminGatewayDtu adminGatewayDtu) {
        UpdateWrapper<AdminGatewayDtu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(AdminGatewayDtu::getDtuId,adminGatewayDtu.getDtuId());
        return this.update(adminGatewayDtu, updateWrapper);
    }


    @Override
    public IPage<AdminGatewayDtu>  getGatewayDtu(QueryRequest request,AdminGatewayDtu adminGatewayDtu) {

        Page<AdminGatewayDtu> page = new Page<>(request.getPageNum(), request.getPageSize());
        return gatewayDtuAdminMapper.selectGatewayDtu(page,adminGatewayDtu);
        }
    }

