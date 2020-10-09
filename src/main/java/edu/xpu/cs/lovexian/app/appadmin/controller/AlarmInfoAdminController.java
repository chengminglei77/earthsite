package edu.xpu.cs.lovexian.app.appadmin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminAlarmInfo;
import edu.xpu.cs.lovexian.app.appadmin.service.IAlarmInfoAdminService;
import edu.xpu.cs.lovexian.app.appadmin.utils.StatusEnum;
import edu.xpu.cs.lovexian.common.annotation.Log;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.common.exception.EarthSiteException;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 *  Controller
 *
 * @author xpu
 * @date 2020-09-21 19:42:21
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/admin/alarmInfo/")
public class AlarmInfoAdminController extends BaseController {
    private String message;
    @Autowired
    private IAlarmInfoAdminService alarmInfoAdminService;


    @GetMapping("list")
    public EarthSiteResponse alarmInfoList(QueryRequest request, AdminAlarmInfo adminAlarmInfo) {
        Map<String, Object> dataTable = getDataTable(this.alarmInfoAdminService.findAlarmInfos(request, adminAlarmInfo));
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    //@Log("报警管理:显示未删除的信息")
    @GetMapping("listByTypeId")
    public EarthSiteResponse getAllinfoByTypeId(QueryRequest request, AdminAlarmInfo adminAlarmInfo) {
        IPage<AdminAlarmInfo> alarmInfos =alarmInfoAdminService.findAlarmInfosByTypeId(request, adminAlarmInfo);
        Map<String, Object> dataTable = getDataTable(alarmInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    //info类专用，其他类删除
    @PostMapping("saveOrUpdate")
    public EarthSiteResponse addOrUpdateAlarmInfo(AdminAlarmInfo adminAlarmInfo) {

        Date date = new Date();
        if (StringUtils.isEmpty(adminAlarmInfo.getId())) {
            adminAlarmInfo.setAlarmTime(date);//创建的时间
            adminAlarmInfo.setStatus(StatusEnum.NORMAL_STATE.getCode());//选择状态
        }
        //adminAlarmInfo.setUpdatedAt(date);//设置最后的更新时间

        //保存或更新dtu信息
        boolean actOper = alarmInfoAdminService.saveOrUpdate(adminAlarmInfo);
        return EarthSiteResponse.SUCCESS().data(actOper);
    }
    /**
     * 搜索(查询)dtu相关信息
     *
     * @param request
     * @param adminAlarmInfo
     * @return
     */
    @Log("dtu管理:查询相关信息")
    @GetMapping("queryDtuInfo")
    public EarthSiteResponse queryDtu(QueryRequest request, AdminAlarmInfo adminAlarmInfo) {
        IPage<AdminAlarmInfo> alarmInfos = alarmInfoAdminService.queryAlarmInfos(request, adminAlarmInfo);
        Map<String, Object> dataTable = getDataTable(alarmInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }
    //@Log("报警管理:批量删除信息")
    @DeleteMapping("BatchDelete/{actionIds}")
    public EarthSiteResponse deleteBatchAlarms(@NotBlank(message = "{required}") @PathVariable String actionIds)
            throws EarthSiteException {
        try{
            //通过逗号进行分割为数组,循环输出进行删除
            String ids[]=actionIds.split(StringPool.COMMA);
            Arrays.stream(ids).forEach(id->this.alarmInfoAdminService.deleteAlarms(id));
        }catch (Exception e){
            message = "批量删除用户失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS().message("批量删除用户成功");
    }
    // @Log("报警管理:通过id进行删除信息")
    @DeleteMapping("deleteById")
    public EarthSiteResponse deleteAlarm(String id) {
        if(alarmInfoAdminService.deleteAlarms(id)){
            return EarthSiteResponse.SUCCESS().message("删除成功");
        }
        return EarthSiteResponse.FAIL().message("删除失败");
    }
    @Log("报警管理:彻底删除信息")
    @DeleteMapping("completelyDelete")
    public EarthSiteResponse CompletelyDelete(String id) {
        if(alarmInfoAdminService.completelyDeletealarmInfo(id)){
            return EarthSiteResponse.SUCCESS().message("彻底删除成功");
        }
        return EarthSiteResponse.FAIL().message("彻底删除失败");
    }
    // @Log("报警管理:通过id进行还原信息")
    @GetMapping("restoreById")
    public EarthSiteResponse restoreAlarm(String id) {
        if(alarmInfoAdminService.restoreAlarms(id)){
            return EarthSiteResponse.SUCCESS().message("还原成功");
        }
        return EarthSiteResponse.FAIL().message("还原失败");
    }

}
