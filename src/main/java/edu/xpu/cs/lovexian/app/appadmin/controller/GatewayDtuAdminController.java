package edu.xpu.cs.lovexian.app.appadmin.controller;

import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGatewayDtu;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGateways;
import edu.xpu.cs.lovexian.app.appadmin.service.IGatewayDtuAdminService;
import edu.xpu.cs.lovexian.app.appadmin.service.IGatewaysAdminService;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 *  Controller
 *
 * @author xpu
 * @date 2020-09-01 21:51:18
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/admin/gatewayDtu/")
public class GatewayDtuAdminController extends BaseController {
    private String message;
    @Autowired
    private IGatewayDtuAdminService gatewayDtuAdminService;


    @GetMapping("list")
    public EarthSiteResponse gatewayDtuList(QueryRequest request, AdminGatewayDtu adminGatewayDtu){
        Map<String, Object> dataTable = getDataTable(this.gatewayDtuAdminService.findGatewayDtus(request, adminGatewayDtu));
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    //info类专用，其他类删除
    @PostMapping("saveOrUpdate")
    public EarthSiteResponse addOrUpdateGatewayDtu (AdminGatewayDtu adminGatewayDtu){
        System.out.println("=========================进入gatewayDtu添加功能========================");
        String currentUserName = getCurrentUser();
        if (StringUtils.isEmpty(adminGatewayDtu.getId())) {
            adminGatewayDtu.setGatewayId(currentUserName);//设置网关标识
            adminGatewayDtu.setDtuId(currentUserName);//设置dtu标识

        }
        //保存或更新信息
        boolean actOper = gatewayDtuAdminService.saveOrUpdate(adminGatewayDtu);
        return EarthSiteResponse.SUCCESS().data(actOper);
    }

}
