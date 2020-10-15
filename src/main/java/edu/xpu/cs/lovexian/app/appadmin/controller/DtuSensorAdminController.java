package edu.xpu.cs.lovexian.app.appadmin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.xpu.cs.lovexian.common.annotation.Log;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtuSensor;
import edu.xpu.cs.lovexian.app.appadmin.service.IDtuSensorAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *  Controller
 *
 * @author xpu
 * @date 2020-09-21 16:34:55
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/admin/dtuSensor/")
public class DtuSensorAdminController extends BaseController {
    private String message;
    @Autowired
    private IDtuSensorAdminService dtuSensorAdminService;


    @GetMapping("list")
    public EarthSiteResponse dtuSensorList(QueryRequest request, AdminDtuSensor adminDtuSensor) {
        Map<String, Object> dataTable = getDataTable(this.dtuSensorAdminService.findDtuSensors(request, adminDtuSensor));
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }
/*
    //info类专用，其他类删除
    @PostMapping("saveOrUpdate")
    public EarthSiteResponse addOrUpdateDtuSensor (AdminDtuSensor adminDtuSensor){

    }*/
@Log("sensorsDtu管理：增加相关的传感器")
@PostMapping("saveOrUpdate")
public  EarthSiteResponse saveOrUpdateDtuSensor(AdminDtuSensor adminDtuSensor)
{
    boolean saveOrUpdate = dtuSensorAdminService.save(adminDtuSensor);
    return EarthSiteResponse.SUCCESS().data(saveOrUpdate);
}


@Log("sensors管理:显示sensors的信息")
    @GetMapping("listByTypeId")
    public EarthSiteResponse getAllinfoByTypeId(QueryRequest request,AdminDtuSensor adminDtuSensor) {
        //根据查询要求调用Service层的查询未删除的ID的方法
        IPage<AdminDtuSensor> sensorsDtuInfos = dtuSensorAdminService.findSensorsByTypeId(request, adminDtuSensor);
        Map<String, Object> dataTable = getDataTable(sensorsDtuInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    @Log("sensors管理：显示sensors信息")
    @PostMapping("selectCheckList")
    public EarthSiteResponse selectCheckInfoByTypeId(AdminDtuSensor adminDtuSensor) {
        //System.out.println(adminDtuSensor.toString());
        QueryRequest request1 = new QueryRequest();
        IPage<AdminDtuSensor> agentChecks = this.dtuSensorAdminService.findAgentChecks(request1, adminDtuSensor);
        Map<String, Object> dataTable = getDataTable(agentChecks);

        return EarthSiteResponse.SUCCESS().data(dataTable);

    }


}
