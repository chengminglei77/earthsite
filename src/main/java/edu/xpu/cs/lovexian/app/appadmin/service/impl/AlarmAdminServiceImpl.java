package edu.xpu.cs.lovexian.app.appadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminAlarmInfo;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtus;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGatewayDtu;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;
import edu.xpu.cs.lovexian.app.appadmin.mapper.AlarmInfoAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.IAlarmInfoAdminService;
import edu.xpu.cs.lovexian.app.appadmin.utils.StatusEnum;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service实现
 *
 * @author xpu
 * @date 2020-09-21 19:42:21
 */
@Service("alarmInfoAdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AlarmAdminServiceImpl extends ServiceImpl<AlarmInfoAdminMapper, AdminAlarmInfo> implements IAlarmInfoAdminService {

    @Autowired
    private AlarmInfoAdminMapper alarmInfoAdminMapper;


    @Override
    public IPage<AdminAlarmInfo> findAlarmInfos(QueryRequest request, AdminAlarmInfo adminAlarmInfo) {
        QueryWrapper<AdminAlarmInfo> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        if (StringUtils.isNotBlank(adminAlarmInfo.getAlarmInfo())) {
            queryWrapper.lambda().like(AdminAlarmInfo::getAlarmInfo, adminAlarmInfo.getAlarmInfo());
        }

        if (adminAlarmInfo.getStatus() != null) {
            queryWrapper.lambda().eq(AdminAlarmInfo::getStatus, adminAlarmInfo.getStatus());
        }

        if (adminAlarmInfo.getCreateTimeFrom() != null && StringUtils.isNotBlank(adminAlarmInfo.getCreateTimeFrom()) && adminAlarmInfo.getCreateTimeTo() != null && StringUtils.isNotBlank(adminAlarmInfo.getCreateTimeTo())) {
            queryWrapper.lambda().between(AdminAlarmInfo::getAlarmTime, adminAlarmInfo.getCreateTimeFrom(), adminAlarmInfo.getCreateTimeTo());
        }

        Page<AdminAlarmInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }


    @Override
    public IPage<AdminAlarmInfo> queryAlarmInfos(QueryRequest request, AdminAlarmInfo adminAlarmInfo) {
        QueryWrapper<AdminAlarmInfo> queryWrapper = new QueryWrapper<>();
        //查询对应字段dtuInfo
        if (adminAlarmInfo.getAlarmTime() != null) {
            queryWrapper.lambda().eq(AdminAlarmInfo::getAlarmTime, adminAlarmInfo.getAlarmTime());
        }
        Page<AdminAlarmInfo> adminDtusPage = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(adminDtusPage, queryWrapper);
    }

    /**
     * 查询网关历史报警信息
     * @param request
     * @param gateId
     * @return
     */
    @Override
    public IPage<AdminAlarmInfo> gatewayAlarmInfoHistory(QueryRequest request, String gateId) {
        Page<AdminAlarmInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        return alarmInfoAdminMapper.gatewayAlarmInfoHistory(page,gateId);
    }

    /**
     * 查询传感器历史报警信息
     * @param
     * @param sensorId
     * @return
     */
    @Override
    public List<AdminAlarmInfo> sensorsAlarmInfoHistory(String sensorId) {
        return alarmInfoAdminMapper.sensorsAlarmInfoHistory(sensorId);
    }

    /**
     * 查询dtu历史报警信息
     * @param request
     * @param dtuId
     * @return
     */
    @Override
    public IPage<AdminAlarmInfo> dtuAlarmInfoHistory(QueryRequest request, String dtuId) {
        Page<AdminDtus> page = new Page<>(request.getPageNum(), request.getPageSize());
        return alarmInfoAdminMapper.dtuAlarmInfoHistory(page,dtuId);
    }


    /**
     * 分页查找和所有的搜索
     *
     * @param request
     * @param adminAlarmInfo
     * @return
     */
    @Override
    public IPage<AdminAlarmInfo> findAlarmInfosByTypeId(QueryRequest request, AdminAlarmInfo adminAlarmInfo) {
        QueryWrapper<AdminAlarmInfo> queryWrapper = new QueryWrapper<>();

        //如果DtuName不为空,那么就模糊查询:dtu名
        //如果两者都符合,那么就SELECT COUNT(1) FROM dtus WHERE dtu_name LIKE '%%' AND dtu_type LIKE '%%' AND status = 0;
        if (adminAlarmInfo.getAlarmTime() != null) {
            queryWrapper.lambda().like(AdminAlarmInfo::getAlarmTime, adminAlarmInfo.getAlarmTime());
        }
        if (adminAlarmInfo.getCreateTimeFrom() != null && StringUtils.isNotBlank(adminAlarmInfo.getCreateTimeFrom()) && adminAlarmInfo.getCreateTimeTo() != null && StringUtils.isNotBlank(adminAlarmInfo.getCreateTimeTo())) {
            queryWrapper.lambda().between(AdminAlarmInfo::getAlarmTime, adminAlarmInfo.getCreateTimeFrom(), adminAlarmInfo.getCreateTimeTo());
        }

        if (adminAlarmInfo.getStatus() != null) {
            //相当于where status=....
            queryWrapper.lambda().eq(AdminAlarmInfo::getStatus, adminAlarmInfo.getStatus());
        }
        if (adminAlarmInfo.getDeleteState() != null) {
            //相当于where status=....
            queryWrapper.lambda().eq(AdminAlarmInfo::getDeleteState, adminAlarmInfo.getDeleteState());
        } else {
            adminAlarmInfo.setDeleteState(StatusEnum.NORMAL_STATE.getCode());//0为未删除状态
            System.out.println("查询为删除数据的标志state==" + adminAlarmInfo.getDeleteState());
            queryWrapper.lambda().eq(AdminAlarmInfo::getDeleteState, adminAlarmInfo.getDeleteState());
        }

        //排除某些字段
        Page<AdminAlarmInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean deleteAlarms(String id) {
        UpdateWrapper<AdminAlarmInfo> updateWrapper = new UpdateWrapper<>();
        //逻辑删除
        updateWrapper.lambda().eq(AdminAlarmInfo::getId, id).set(AdminAlarmInfo::getDeleteState, 1);
        return this.update(updateWrapper);
    }

    @Override
    public boolean restoreAlarms(String id) {
        UpdateWrapper<AdminAlarmInfo> updateWrapper = new UpdateWrapper<>();
        //还原逻辑删除的报警信息
        updateWrapper.lambda().eq(AdminAlarmInfo::getId, id).set(AdminAlarmInfo::getDeleteState, 0);
        return this.update(updateWrapper);
    }

    @Override
    public boolean completelyDeletealarmInfo(String id) {
        UpdateWrapper<AdminAlarmInfo> updateWrapper = new UpdateWrapper<>();
        //彻底删除
        alarmInfoAdminMapper.deleteById(id);
        return true;

    }
}
