package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import edu.xpu.cs.lovexian.app.appadmin.entity.AdminLawerInfo;
import edu.xpu.cs.lovexian.app.appadmin.mapper.LawerInfoAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.ILawerInfoAdminService;
import edu.xpu.cs.lovexian.common.domain.EarthSiteConstant;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;

import edu.xpu.cs.lovexian.common.utils.SortUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 律师信息表 Service实现
 *
 * @author xpu
 * @date 2019-12-25 15:25:48
 */
@Service("lawerInfoAdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LawerInfoAdminServiceImpl extends ServiceImpl<LawerInfoAdminMapper, AdminLawerInfo> implements ILawerInfoAdminService {

    @Autowired
    private LawerInfoAdminMapper lawerInfoAdminMapper;

    @Override
    public IPage<AdminLawerInfo> findLawerInfos(QueryRequest request, AdminLawerInfo adminLawerInfo) {
        QueryWrapper<AdminLawerInfo> queryWrapper = new QueryWrapper<>();
        Page<AdminLawerInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        //SortUtil.handlePageSort(request, page, "isTop#updateTime#createTime", LoveXianConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean updateLawerInfo(AdminLawerInfo adminLawerInfo) {
        UpdateWrapper<AdminLawerInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(AdminLawerInfo::getId, adminLawerInfo.getId());
        return this.update(adminLawerInfo, updateWrapper);
    }


    @Override
    public IPage<AdminLawerInfo> findLawerInfosByTypeId(QueryRequest request, AdminLawerInfo adminLawerInfo) {
        QueryWrapper<AdminLawerInfo> queryWrapper = new QueryWrapper<>();
//        //查询对应语言的lawInfo
//        if (adminLawerInfo.getLanguageType()!=null){
//            queryWrapper.lambda().eq(AdminLawerInfo::getLanguageType, adminLawerInfo.getLanguageType());
//        }
        if (adminLawerInfo.getCreateTimeFrom() != null && StringUtils.isNotBlank(adminLawerInfo.getCreateTimeFrom()) && adminLawerInfo.getCreateTimeTo() != null && StringUtils.isNotBlank(adminLawerInfo.getCreateTimeTo())) {
            queryWrapper.lambda().between(AdminLawerInfo::getCreateTime, adminLawerInfo.getCreateTimeFrom(), adminLawerInfo.getCreateTimeTo());
        }
        //查询未删除的内容
        if (StringUtils.isNotBlank(adminLawerInfo.getLawerName())) {
            queryWrapper.lambda().like(AdminLawerInfo::getLawerName, adminLawerInfo.getLawerName());
        }
        if (adminLawerInfo.getDelState() != null) {
            queryWrapper.lambda().eq(AdminLawerInfo::getDelState, adminLawerInfo.getDelState());
        } else {
            adminLawerInfo.setDelState(0);
            System.out.println("查询为删除数据的标志del_state==" + adminLawerInfo.getDelState());
            queryWrapper.lambda().eq(AdminLawerInfo::getDelState, adminLawerInfo.getDelState());
        }
        if (adminLawerInfo.getCheckState() != null) {
            queryWrapper.lambda().eq(AdminLawerInfo::getCheckState, adminLawerInfo.getCheckState());
        }

        //排除某些字段
        Page<AdminLawerInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        //排序规则设置，使用'#'分隔不同的参数，从左往右的优先级排序
        SortUtil.handlePageSort(request, page, "isTop#updateTime#createTime", EarthSiteConstant.ORDER_DESC, true);
        return this.page(page, queryWrapper);
    }

    @Override
    public IPage<AdminLawerInfo> queryLawerInfo(QueryRequest request, AdminLawerInfo adminLawerInfo) {
        QueryWrapper<AdminLawerInfo> queryWrapper = new QueryWrapper<>();
        //查询对应字段lawInfo
        if (adminLawerInfo.getLawerName() != null) {
            queryWrapper.lambda().eq(AdminLawerInfo::getLawerName, adminLawerInfo.getLawerName());
        }

        if (adminLawerInfo.getCheckState() != null) {
            queryWrapper.lambda().eq(AdminLawerInfo::getCheckState, adminLawerInfo.getCheckState());
        }

        Page<AdminLawerInfo> adminLawerInfoPage = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(adminLawerInfoPage, queryWrapper);
    }

    @Override
    public boolean deleteBatchLawer(String id) {
        UpdateWrapper<AdminLawerInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(AdminLawerInfo::getId, id).set(AdminLawerInfo::getDelState, 1);
        return this.update(updateWrapper);
    }

    @Override
    public boolean deleteLawerInfo(String id) {
        UpdateWrapper<AdminLawerInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(AdminLawerInfo::getId, id).set(AdminLawerInfo::getDelState, 1);
        return this.update(updateWrapper);
    }


}
