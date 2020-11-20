package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCommandInfo;
import edu.xpu.cs.lovexian.app.appadmin.mapper.CommandInfoAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.ICommandInfoAdminService;
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
 * @date 2020-09-21 19:42:21
 */
@Service("commandInfoAdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CommandAdminServiceImpl extends ServiceImpl<CommandInfoAdminMapper, AdminCommandInfo> implements ICommandInfoAdminService {

    @Autowired
    private CommandInfoAdminMapper commandInfoAdminMapper;


    @Override
    public IPage<AdminCommandInfo> findCommandInfos(QueryRequest request, AdminCommandInfo adminCommandInfo) {
        QueryWrapper<AdminCommandInfo> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
      /*  if (StringUtils.isNotBlank(adminCommandInfo.getCommand())) {
            queryWrapper.lambda().like(AdminCommandInfo::getCommand, adminCommandInfo.getCommand());
        }*/

        /*if (adminCommandInfo.getCommand() != null) {
            queryWrapper.lambda().eq(AdminCommandInfo::getCommand, adminCommandInfo.getCommand());
        }*/
        /*else {
            adminCommandInfo.setStatus(StatusEnum.NORMAL_STATE.getCode());//0为未删除状态
            System.out.println("查询为删除数据的标志status==" + adminCommandInfo.getStatus());
            queryWrapper.lambda().eq(AdminCommandInfo::getStatus, adminCommandInfo.getStatus());
        }*/

      if(adminCommandInfo.getSendTimeFrom()!=null&&StringUtils.isNotBlank(adminCommandInfo.getSendTimeFrom()) &&adminCommandInfo.getSendTimeTo()!=null&&StringUtils.isNotBlank(adminCommandInfo.getSendTimeTo())){
           queryWrapper.lambda().between(AdminCommandInfo::getSendTime,adminCommandInfo.getSendTimeFrom(),adminCommandInfo.getSendTimeTo());
       }

        Page<AdminCommandInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }



    @Override
    public IPage<AdminCommandInfo> queryCommandInfos(QueryRequest request, AdminCommandInfo adminCommandInfo) {
        QueryWrapper<AdminCommandInfo> queryWrapper = new QueryWrapper<>();
        //查询对应字段dtuInfo
        if (adminCommandInfo.getSendTime() != null) {
            queryWrapper.lambda().eq(AdminCommandInfo::getSendTime, adminCommandInfo.getSendTime());
        }
        Page<AdminCommandInfo> adminDtusPage = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(adminDtusPage, queryWrapper);
    }

    /**
     * 分页查找和所有的搜索
     * @param request
     * @param adminCommandInfo
     * @return
     */
    @Override
    public IPage<AdminCommandInfo> findCommandInfosByTypeId(QueryRequest request, AdminCommandInfo adminCommandInfo) {
        QueryWrapper<AdminCommandInfo> queryWrapper = new QueryWrapper<>();

       /* if (StringUtils.isNotBlank(adminCommandInfo.getCommand())) {
            queryWrapper.lambda().like(AdminCommandInfo::getCommand, adminCommandInfo.getCommand());
        }*/
        //如果DtuName不为空,那么就模糊查询:dtu名
        //如果两者都符合,那么就SELECT COUNT(1) FROM dtus WHERE dtu_name LIKE '%%' AND dtu_type LIKE '%%' AND status = 0;
        if (adminCommandInfo.getSendTime() != null) {
            queryWrapper.lambda().eq(AdminCommandInfo::getSendTime, adminCommandInfo.getSendTime());
        }
        if(adminCommandInfo.getSendTimeFrom()!=null&&StringUtils.isNotBlank(adminCommandInfo.getSendTimeFrom()) &&adminCommandInfo.getSendTimeTo()!=null&&StringUtils.isNotBlank(adminCommandInfo.getSendTimeTo())){
            queryWrapper.lambda().between(AdminCommandInfo::getSendTime,adminCommandInfo.getSendTimeFrom(),adminCommandInfo.getSendTimeTo());
        }
        if (adminCommandInfo.getStatus() != null) {
            queryWrapper.lambda().eq(AdminCommandInfo::getStatus, adminCommandInfo.getStatus());
        }
        /*else {
            adminCommandInfo.setStatus(StatusEnum.NORMAL_STATE.getCode());//0为未删除状态
            System.out.println("查询为删除数据的标志status==" + adminCommandInfo.getStatus());
            queryWrapper.lambda().eq(AdminCommandInfo::getStatus, adminCommandInfo.getStatus());
        }*/
        //排除某些字段
        Page<AdminCommandInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

}
