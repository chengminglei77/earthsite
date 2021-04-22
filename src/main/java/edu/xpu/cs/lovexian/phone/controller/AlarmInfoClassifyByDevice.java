package edu.xpu.cs.lovexian.phone.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminAlarmInfo;
import edu.xpu.cs.lovexian.app.appadmin.mapper.SelectDTUAlarmInfo;
import edu.xpu.cs.lovexian.app.appadmin.mapper.SelectSensorAlarmInfo;
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
import java.util.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/alarmInfoClassify/")
public class AlarmInfoClassifyByDevice extends BaseController {
    private String message;
    @Autowired
    private IAlarmInfoAdminService alarmInfoAdminService;
    @Autowired
    private SelectDTUAlarmInfo DTUAlarmInfo;
    @Autowired
    private SelectSensorAlarmInfo SensorAlarmInfo;


    @GetMapping("list")
    public EarthSiteResponse alarmInfoList(QueryRequest request, AdminAlarmInfo adminAlarmInfo) {
        Map<String, Object> dataTable = getDataTable(this.alarmInfoAdminService.findAlarmInfos(request, adminAlarmInfo));
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    //@Log("报警管理:显示未删除的信息")
    @GetMapping("listByTypeId")
    public EarthSiteResponse getAllinfoByTypeId(QueryRequest request, AdminAlarmInfo adminAlarmInfo) {
        IPage<AdminAlarmInfo> alarmInfos = alarmInfoAdminService.findAlarmInfosByTypeId(request, adminAlarmInfo);
        Map<String, Object> dataTable = getDataTable(alarmInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }



    @GetMapping("getAllDTUinfo")
    public EarthSiteResponse getAllDTUinfo() {
        List<AdminAlarmInfo> adminAlarmInfoList=DTUAlarmInfo.selectDTUAlarmInfo();
        return EarthSiteResponse.SUCCESS().data(adminAlarmInfoList);
    }

    @GetMapping("getAllSensorsinfo")
    public EarthSiteResponse getAllSensorsinfo() {
        List<AdminAlarmInfo> adminAlarmInfoList=SensorAlarmInfo.selectSensorAlarmInfo();
        return EarthSiteResponse.SUCCESS().data(adminAlarmInfoList);
    }



}
