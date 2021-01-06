package edu.xpu.cs.lovexian.app.appadmin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminUnresovledData;
import edu.xpu.cs.lovexian.app.appadmin.service.ICmdstatisticsAdminService;
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
 * @date 2020-09-01 17:01:18
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/admin/cmdstatistics/")
public class CmdstatisticsAdminController extends BaseController {
    private String message;
    @Autowired
    private ICmdstatisticsAdminService cmdstatisticsAdminService;

    @GetMapping("list")
    public EarthSiteResponse gatewaysList(QueryRequest request, AdminUnresovledData adminUnresovledData) {
        Map<String, Object> dataTable = getDataTable(this.cmdstatisticsAdminService.findCmds(request, adminUnresovledData));
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }
    @Log("网关管理:显示未删除的的信息")
    @GetMapping("listByTypeId")
    public EarthSiteResponse getAllinfoByTypeId(QueryRequest request, AdminUnresovledData adminUnresovledData) {
        IPage<AdminUnresovledData> gatewayInfos = cmdstatisticsAdminService.findCmdsByTypeId(request, adminUnresovledData);
        System.out.println(gatewayInfos);
        Map<String, Object> dataTable = getDataTable(gatewayInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }


}