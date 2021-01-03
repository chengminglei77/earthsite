package edu.xpu.cs.lovexian.app.appadmin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGateways;
import edu.xpu.cs.lovexian.app.appadmin.service.IGatewaysAdminService;
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
 * Controller
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
    public EarthSiteResponse gatewaysList(QueryRequest request, AdminGateways adminGateways) {
        Map<String, Object> dataTable = getDataTable(this.gatewaysAdminService.findGatewayss(request, adminGateways));
        return EarthSiteResponse.SUCCESS().data(dataTable);

    }


    @Log("网关管理:显示未删除的的信息")
    @GetMapping("listByTypeId")
    public EarthSiteResponse getAllinfoByTypeId(QueryRequest request, AdminGateways adminGateways) {
        IPage<AdminGateways> gatewayInfos = gatewaysAdminService.findGatewaysByTypeId(request, adminGateways);
        System.out.println(gatewayInfos);
        Map<String, Object> dataTable = getDataTable(gatewayInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    @Log("网关管理:通过id进行删除信息")
    @DeleteMapping("deleteById")
    public EarthSiteResponse deleteGateway(String id) {
        if (gatewaysAdminService.deleteGateWays(id)) {
            return EarthSiteResponse.SUCCESS().message("删除成功");
        }
        return EarthSiteResponse.FAIL().message("删除失败");
    }

    @Log("网关管理:批量删除信息")
    @DeleteMapping("BatchDelete/{actionIds}")
    public EarthSiteResponse deleteBatchGateways(@NotBlank(message = "{required}") @PathVariable String actionIds)
            throws EarthSiteException {
        try {
            //通过逗号进行分割为数组,循环输出进行删除
            String ids[] = actionIds.split(StringPool.COMMA);
            Arrays.stream(ids).forEach(id -> this.gatewaysAdminService.deleteGateWays(id));
        } catch (Exception e) {
            message = "批量删除用户失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS().message("批量删除用户成功");
    }


    //info类专用，其他类删除
    //@log("网关管理")
    @PostMapping("saveOrUpdate")
    public EarthSiteResponse addOrUpdateGateways(AdminGateways adminGateways) {
        System.out.println("=========================进入Gateways添加功能========================");
        System.out.println("deleteState=" + adminGateways.getDeleteState());
        if (adminGateways.getStatus() == null) {
            System.out.println("status没有获得到值");//如果输出的是null代表status没有获得到值
        } else {
            System.out.println("status=" + adminGateways.getStatus());
        }

        String currentPort = getCurrentUser();
        Date date = new Date();
        if (StringUtils.isEmpty(adminGateways.getId())) {
            //adminGateways.setServerPort(currentPort);//设置网关的端口号
            adminGateways.setCreatedAt(date);//网关的部署时间
            adminGateways.setDeleteState(StatusEnum.NORMAL_STATE.getCode());//将初始的网关删除状态自动设置为0
        }
        if (adminGateways.getStatus() == null) {
            adminGateways.setStatus(StatusEnum.ABNORMAL_STATE.getCode());//当未选择网关状态时，默认网关状态为离线
        }
        adminGateways.setUpdatedAt(date);//设置最后的更新时间
        //保存或更新dtu信息
        boolean actOper = gatewaysAdminService.saveOrUpdate(adminGateways);

        return EarthSiteResponse.SUCCESS().data(actOper);


    }

    @Log("网关管理:彻底删除信息")
    @DeleteMapping("completelyDelete")
    public EarthSiteResponse CompletelyDelete(String id) {
        if (gatewaysAdminService.completelyDeleteGateway(id)) {
            return EarthSiteResponse.SUCCESS().message("彻底删除成功");
        }
        return EarthSiteResponse.FAIL().message("彻底删除失败");
    }

    /**
     * 搜索相关信息
     *
     * @param request
     * @param adminGateways
     * @return
     */
    @Log("网关管理:查询相关信息")
    @GetMapping("queryGateways")
    public EarthSiteResponse queryGateways(QueryRequest request, AdminGateways adminGateways) {
        IPage<AdminGateways> gatewayInfos = gatewaysAdminService.queryGateways(request, adminGateways);
        Map<String, Object> dataTable = getDataTable(gatewayInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    @Log("还原网关信息")
    @PostMapping("restoreById")
    public EarthSiteResponse restoreGateways(String id) {
        if (gatewaysAdminService.restoreGateways(id)) {
            return EarthSiteResponse.SUCCESS().message("还原成功");
        }
        return EarthSiteResponse.FAIL().message("还原失败");
    }
}