package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminUnresovledData;
import edu.xpu.cs.lovexian.app.appadmin.mapper.CmdstatisticsAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.ICmdstatisticsAdminService;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service实现
 *
 * @author xpu
 * @date 2020-09-01 17:01:18
 */
@Service("CmdstatisticsAdminServiceService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CmdstatisticsAdminServicelmpl extends ServiceImpl<CmdstatisticsAdminMapper, AdminUnresovledData> implements ICmdstatisticsAdminService {
    @Autowired
    private CmdstatisticsAdminMapper gatewaysAdminMapper;

    @Override
    public IPage<AdminUnresovledData> findCmds(QueryRequest request, AdminUnresovledData adminUnresovledData) {
        QueryWrapper<AdminUnresovledData> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        Page<AdminUnresovledData> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public IPage<AdminUnresovledData> findCmdsByTypeId(QueryRequest request, AdminUnresovledData adminUnresovledData) {
        QueryWrapper<AdminUnresovledData> queryWrapper = new QueryWrapper<>();
        //模糊查询
        if (StringUtils.isNotBlank(adminUnresovledData.getId())) {
            queryWrapper.lambda().like(AdminUnresovledData::getId, adminUnresovledData.getId());
        }
        if (StringUtils.isNotBlank(adminUnresovledData.getSettingID())) {
            if (adminUnresovledData.getSettingID().equals("A8")) {
                queryWrapper.lambda().eq(AdminUnresovledData::getInstructionType, adminUnresovledData.getSettingID());
            } else {
                queryWrapper.lambda().like(AdminUnresovledData::getSettingID, adminUnresovledData.getSettingID());
            }
        }
        queryWrapper.lambda().orderByDesc(AdminUnresovledData::getColTime);
       /* if (adminUnresovledData.getStatus() != null) {
            //相当于where status=....
            queryWrapper.lambda().eq(AdminUnresovledData::getStatus, adminUnresovledData.getStatus()).orderByDesc(AdminUnresovledData::getCreatedAt);
        }
        if (adminUnresovledData.getDeleteState() != null){
            queryWrapper.lambda().eq(AdminUnresovledData::getDeleteState,adminUnresovledData.getDeleteState()).orderByDesc(AdminUnresovledData::getCreatedAt);
        }else {
            adminUnresovledData.setDeleteState(StatusEnum.NORMAL_STATE.getCode());//0为未删除状态
            System.out.println("查询为删除数据的标志deleteState==" + adminUnresovledData.getDeleteState());
            queryWrapper.lambda().eq(AdminUnresovledData::getDeleteState, adminUnresovledData.getDeleteState()).orderByDesc(AdminUnresovledData::getCreatedAt);
        }*/

        Page<AdminUnresovledData> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public IPage<AdminUnresovledData> queryCmds(QueryRequest request, AdminUnresovledData adminUnresovledData) {
        QueryWrapper<AdminUnresovledData> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(adminUnresovledData.getSettingID())) {
            queryWrapper.lambda().like(AdminUnresovledData::getSettingID, adminUnresovledData.getSettingID());
        }
        Page<AdminUnresovledData> Page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(Page, queryWrapper);
    }
}