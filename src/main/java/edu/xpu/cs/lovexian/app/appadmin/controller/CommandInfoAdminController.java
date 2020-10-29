package edu.xpu.cs.lovexian.app.appadmin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCommandInfo;
import edu.xpu.cs.lovexian.app.appadmin.service.ICommandInfoAdminService;
import edu.xpu.cs.lovexian.common.annotation.Log;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/admin/commandInfo/")
public class CommandInfoAdminController extends BaseController {
    private String message;
    @Autowired
    private ICommandInfoAdminService commandInfoAdminService;


    @GetMapping("list")
    public EarthSiteResponse commandInfoList(QueryRequest request, AdminCommandInfo adminCommandInfo) {
        Map<String, Object> dataTable = getDataTable(this.commandInfoAdminService.findCommandInfos(request, adminCommandInfo));
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    //@Log("报警管理:显示未删除的信息")
    //显示出列表++++查询
    @GetMapping("listByTypeId")
    public EarthSiteResponse getAllinfoByTypeId(QueryRequest request, AdminCommandInfo adminCommandInfo) {
        IPage<AdminCommandInfo> commandInfos =commandInfoAdminService.findCommandInfosByTypeId(request, adminCommandInfo);
        Map<String, Object> dataTable = getDataTable(commandInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    /**
     * 搜索(查询)dtu相关信息
     *
     * @param request
     * @param adminCommandInfo
     * @return
     */
    @Log("dtu管理:查询相关信息")
    @GetMapping("queryCommandInfo")
    public EarthSiteResponse queryCommand(QueryRequest request, AdminCommandInfo adminCommandInfo) {
        IPage<AdminCommandInfo> commandInfos = commandInfoAdminService.queryCommandInfos(request, adminCommandInfo);
        Map<String, Object> dataTable = getDataTable(commandInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }
}
