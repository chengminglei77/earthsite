package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
        if (StringUtils.isNotBlank(adminCommandInfo.getCommand())) {
            queryWrapper.lambda().like(AdminCommandInfo::getCommand, adminCommandInfo.getCommand());
        }

        if (adminCommandInfo.getStatus() != null) {
            queryWrapper.lambda().eq(AdminCommandInfo::getStatus, adminCommandInfo.getStatus());
        }

//        if(adminCommandInfo.getCreateTimeFrom()!=null&&StringUtils.isNotBlank(adminCommandInfo.getCreateTimeFrom()) &&adminCommandInfo.getCreateTimeTo()!=null&&StringUtils.isNotBlank(adminCommandInfo.getCreateTimeTo())){
//            queryWrapper.lambda().between(AdminCommandInfo::getReceiveTime,adminCommandInfo.getCreateTimeFrom(),adminCommandInfo.getCreateTimeTo());
//        }

        Page<AdminCommandInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }



    @Override
    public IPage<AdminCommandInfo> queryCommandInfos(QueryRequest request, AdminCommandInfo adminCommandInfo) {
        QueryWrapper<AdminCommandInfo> queryWrapper = new QueryWrapper<>();
        //查询对应字段dtuInfo
        if (adminCommandInfo.getReceiveTime() != null) {
            queryWrapper.lambda().eq(AdminCommandInfo::getReceiveTime, adminCommandInfo.getReceiveTime());
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

        //如果DtuName不为空,那么就模糊查询:dtu名
        //如果两者都符合,那么就SELECT COUNT(1) FROM dtus WHERE dtu_name LIKE '%%' AND dtu_type LIKE '%%' AND status = 0;
        if (adminCommandInfo.getSendTime()!=null) {
            queryWrapper.lambda().like(AdminCommandInfo::getSendTime, adminCommandInfo.getSendTime());
        }
//        if(adminCommandInfo.getCreateTimeFrom()!=null&&StringUtils.isNotBlank(adminCommandInfo.getCreateTimeFrom()) &&adminCommandInfo.getCreateTimeTo()!=null&&StringUtils.isNotBlank(adminCommandInfo.getCreateTimeTo())){
//            queryWrapper.lambda().between(AdminCommandInfo::getReceiveTime,adminCommandInfo.getCreateTimeFrom(),adminCommandInfo.getCreateTimeTo());
//        }

        if (adminCommandInfo.getStatus() != null) {
            //相当于where status=....
            queryWrapper.lambda().eq(AdminCommandInfo::getStatus, adminCommandInfo.getStatus());
        }
        if(adminCommandInfo.getCommand()!=null){
            queryWrapper.lambda().eq(AdminCommandInfo::getCommand,adminCommandInfo.getCommand());
        }
        if(adminCommandInfo.getDescription()!=null){
            queryWrapper.lambda().eq(AdminCommandInfo::getDescription,adminCommandInfo.getDescription());
        }
//        if (adminCommandInfo.getDeleteState() != null) {
//            //相当于where status=....
//            queryWrapper.lambda().eq(AdminCommandInfo::getDeleteState, adminCommandInfo.getDeleteState());
//        } else {
//            adminCommandInfo.setDeleteState(StatusEnum.NORMAL_STATE.getCode());//0为未删除状态
//            System.out.println("查询为删除数据的标志state==" + adminCommandInfo.getDeleteState());
//            queryWrapper.lambda().eq(AdminCommandInfo::getDeleteState, adminCommandInfo.getDeleteState());
//        }

        //排除某些字段
        Page<AdminCommandInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean deleteCommands(String id) {
        UpdateWrapper<AdminCommandInfo> updateWrapper = new UpdateWrapper<>();
        //逻辑删除
        updateWrapper.lambda().eq(AdminCommandInfo::getId, id).set(AdminCommandInfo::getStatus, 1);
        return this.update(updateWrapper);
    }
    @Override
    public boolean restoreCommands(String id) {
        UpdateWrapper<AdminCommandInfo> updateWrapper = new UpdateWrapper<>();
        //还原逻辑删除的报警信息
        updateWrapper.lambda().eq(AdminCommandInfo::getId, id).set(AdminCommandInfo::getStatus, 0);
        return this.update(updateWrapper);
    }
    @Override
    public boolean completelyDeleteCommandInfo(String id) {
        UpdateWrapper<AdminCommandInfo> updateWrapper = new UpdateWrapper<>();
        //彻底删除
        commandInfoAdminMapper.deleteById(id);
        return true;

    }


}
