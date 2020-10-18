package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGateways;
import edu.xpu.cs.lovexian.app.appadmin.mapper.GatewaysAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IGatewaysAdminService;
import edu.xpu.cs.lovexian.app.appadmin.utils.StatusEnum;
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
        if (StringUtils.isNotBlank(adminGateways.getGateId())) {
            queryWrapper.lambda().like(AdminGateways::getGateId, adminGateways.getGateId());
        }


        if (adminGateways.getStatus() != null) {
            //相当于where status=....
            queryWrapper.lambda().eq(AdminGateways::getStatus, adminGateways.getStatus());
        }
        if (adminGateways.getDeleteState() != null){
            queryWrapper.lambda().eq(AdminGateways::getDeleteState,adminGateways.getDeleteState());
        }else {
            adminGateways.setDeleteState(StatusEnum.NORMAL_STATE.getCode());//0为未删除状态
            System.out.println("查询为删除数据的标志state==" + adminGateways.getDeleteState());
            queryWrapper.lambda().eq(AdminGateways::getDeleteState, adminGateways.getDeleteState());
        }
        Page<AdminGateways> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean deleteGateWays(String id) {
        UpdateWrapper<AdminGateways> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(AdminGateways::getId,id).set(AdminGateways::getDeleteState,1);
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

    @Override
    public boolean completelyDeleteGateway(String id) {

        //删除操作实际上做的是将status设置为1,从而不是真正意义上的在数据库删除,只是不在前端界面显示而已
        gatewaysAdminMapper.deleteById(id);

        return true;

    }

    @Override
    public boolean restoreGateways(String id) {
        UpdateWrapper<AdminGateways> updateWrapper = new UpdateWrapper<>();
        //还原逻辑删除的报警信息
        updateWrapper.lambda().eq(AdminGateways::getId, id).set(AdminGateways::getDeleteState, 0);
        return this.update(updateWrapper);
    }

    @Override
    public boolean selectDtus(String dtuId) {
        return false;
    }


}
