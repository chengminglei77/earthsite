package edu.xpu.cs.lovexian.app.appadmin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensors;
import edu.xpu.cs.lovexian.app.appadmin.service.ISensorsAdminService;
import edu.xpu.cs.lovexian.app.appadmin.utils.StatusEnum;
import edu.xpu.cs.lovexian.common.annotation.Log;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.common.exception.EarthSiteException;
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
 * @date 2020-09-01 22:02:59
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/admin/sensors/")
public class SensorsAdminController extends BaseController {
    private String message;
    @Autowired
    private ISensorsAdminService sensorsAdminService;


    @GetMapping("list")
    public EarthSiteResponse sensorsList(QueryRequest request, AdminSensors adminSensors) {
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setPageNum(1);
        queryRequest.setPageSize(20);
        Map<String, Object> dataTable = getDataTable(this.sensorsAdminService.findSensorss(queryRequest, adminSensors));
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    @Log("sensors管理:通过id删除信息")
    @DeleteMapping("deleteById")
    public EarthSiteResponse deleteSensors(String id) {
        //调用Service层所对应的删除ID的方法
        if (sensorsAdminService.deleteSensors(id)) {
            //如果成功
            return EarthSiteResponse.SUCCESS().message("删除成功");
        }
        //失败
        return EarthSiteResponse.FAIL().message("删除失败");
    }

    /*//info类专用，其他类删除
    @PostMapping("saveOrUpdate")
    public EarthSiteResponse addOrUpdateSensors (AdminSensors adminSensors){

    }*/
    @Log("sensors管理:查询相关信息")
    @GetMapping("querySensorsInfo")
    public EarthSiteResponse querySensors(QueryRequest request, AdminSensors adminSensors) {
        //根据要求调用Service层的模糊查询的方法
        IPage<AdminSensors> sensorsInfos = sensorsAdminService.querySensorsInfo(request, adminSensors);
        //将数据转化为集合
        Map<String, Object> dataTable = getDataTable(sensorsInfos);
        //将数据填充到EarthSiteResponse并且返回
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    @Log("sensors管理:显示sensors的信息")
    @GetMapping("listByTypeId")
    public EarthSiteResponse getAllinfoByTypeId(QueryRequest request, AdminSensors adminSensors) {
        //根据查询要求调用Service层的查询未删除的ID的方法
        IPage<AdminSensors> sensorsInfos = sensorsAdminService.findSensorsByTypeId(request, adminSensors);
        Map<String, Object> dataTable = getDataTable(sensorsInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    @Log("sensors管理:保存或更新相关信息")
    @PostMapping("saveOrUpdate")
    public EarthSiteResponse addOrUpdateSensors(AdminSensors adminSensors) {
        log.info("保存或更新相关信息");
        //System.out.println("=========================进入Sensors添加功能========================");
        Date date = new Date();
        //String current = getCurrentUser();
        //如果当前id不为空
        if (StringUtils.isEmpty(adminSensors.getId())) {
            // adminSensors.setSensorId(current);
            adminSensors.setCreatedAt(date);//创建的时间
            //adminSensors.setStatus(StatusEnum.NORMAL_STATE.getCode());//设置状态
            adminSensors.setStatus(StatusEnum.NORMAL_STATE.getCode());
        }

        adminSensors.setUpdatedAt(date);//设置Sensors最后更新时间

        //保存或更新Sensors信息
        boolean actOper = sensorsAdminService.saveOrUpdate(adminSensors);
        return EarthSiteResponse.SUCCESS().data(actOper);

    }

    @Log("sensors管理:批量删除")
    @DeleteMapping("BatchDelete/{actionIds}")
    public EarthSiteResponse deleteBatchSensors(@NotBlank(message = "{required}") @PathVariable String actionIds)
            throws EarthSiteException {
        try {
            //通过逗号进行分割为数组,循环输出进行删除
            String ids[] = actionIds.split(StringPool.COMMA);
            Arrays.stream(ids).forEach(id -> this.sensorsAdminService.deleteSensors(id));
        } catch (Exception e) {
            message = "批量删除用户失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS().message("批量删除用户成功");
    }

    @Log("sensors管理：彻底删除")
    @DeleteMapping("completelyDelete")
    public EarthSiteResponse completelyDelete(String id) {
        if (sensorsAdminService.completelyDeleteSensors(id)) {
            return EarthSiteResponse.SUCCESS().message("删除成功");
        }
        return EarthSiteResponse.FAIL().message("删除失败");

    }

    @PostMapping("restoreById")
    public EarthSiteResponse restoreById(String id) {
        if (sensorsAdminService.restoreById(id)) {
            return EarthSiteResponse.SUCCESS().message("还原成功");
        }
        return EarthSiteResponse.FAIL().message("还原失败");
    }

    @Log("sensors管理：查询传感器数据的数据终端设备地址")
    @GetMapping("querySensorAdress")
    public EarthSiteResponse querySensorAdress(String message) {
        String s = sensorsAdminService.querySensorAdress(message);
        return EarthSiteResponse.SUCCESS().data(s);
    }

}
