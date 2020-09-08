package edu.xpu.cs.lovexian.app.appadmin.controller;

import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGateways;
import edu.xpu.cs.lovexian.app.appadmin.service.IGatewaysAdminService;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/admin/gateways/")
public class GatewaysAdminController extends BaseController {
    private String message;
    @Autowired
    private IGatewaysAdminService gatewaysAdminService;


    @GetMapping("list")
    public EarthSiteResponse gatewaysList(QueryRequest request,AdminGateways adminGateways){
        Map<String, Object> dataTable = getDataTable(this.gatewaysAdminService.findGatewayss(request, adminGateways));
        return EarthSiteResponse.SUCCESS().data(dataTable);

    }
    //info类专用，其他类删除
    @PostMapping("saveOrUpdate")
    public void addOrUpdateGateways (AdminGateways adminGateways){

    }
}
