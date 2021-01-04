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

    @Override
    public IPage<AdminUnresovledData> findCmds(QueryRequest request, AdminUnresovledData uData) {
        QueryWrapper<AdminUnresovledData> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        Page<AdminUnresovledData> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public IPage<AdminUnresovledData> findCmdsByTypeId(QueryRequest request, AdminUnresovledData uData) {
        QueryWrapper<AdminUnresovledData> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(uData.getSettingID())) {
            if (uData.getSettingID().equals("A8")) {
                queryWrapper.lambda().eq(AdminUnresovledData::getInstructionType, uData.getSettingID());
            } else {
                queryWrapper.lambda().like(AdminUnresovledData::getSettingID, uData.getSettingID());
            }
        }
        if(StringUtils.isNotBlank(uData.getFrameNum())){
            queryWrapper.lambda().like(AdminUnresovledData::getSettingID, uData.getFrameNum());
        }
        queryWrapper.lambda().orderByDesc(AdminUnresovledData::getColTime);
        Page<AdminUnresovledData> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }
    
}