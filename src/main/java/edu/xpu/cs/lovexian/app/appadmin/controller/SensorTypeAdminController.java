package edu.xpu.cs.lovexian.app.appadmin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminSensorType;
import edu.xpu.cs.lovexian.app.appadmin.service.ISensorTypeAdminService;
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
import java.util.Map;

/**
 * Controller
 *
 * @author xpu
 * @date 2020-09-27 19:33:26
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/admin/sensorType/")
public class SensorTypeAdminController extends BaseController {
    private String message;
    @Autowired
    private ISensorTypeAdminService sensorTypeAdminService;


    @GetMapping("list")
    public EarthSiteResponse sensorTypeList(QueryRequest request, AdminSensorType adminSensorType) {
        Map<String, Object> dataTable = getDataTable(this.sensorTypeAdminService.findSensorTypes(request, adminSensorType));
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    /* //info类专用，其他类删除
     @PostMapping("saveOrUpdate")
     public EarthSiteResponse addOrUpdateSensorType (AdminSensorType adminSensorType){
 return  null;
     }*/
    @Log("sensorsType管理:显示sensorsType的信息")
    @GetMapping("listByTypeId")
    public EarthSiteResponse getAllinfoByTypeId(QueryRequest request, AdminSensorType adminSensorType) {
        //根据查询要求调用Service层的查询未删除的ID的方法
        IPage<AdminSensorType> sensorsTypeByTypeId = sensorTypeAdminService.findSensorsTypeByTypeId(request, adminSensorType);
        Map<String, Object> dataTable = getDataTable(sensorsTypeByTypeId);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    @Log("sensorsType管理：删除对应的传感器类型(逻辑删除)")
    @DeleteMapping("deleteById")
    public EarthSiteResponse deleteById(String id) {
        if (sensorTypeAdminService.deleteSensorsTypeByid(id))
            return EarthSiteResponse.SUCCESS().message("删除成功");
        else
            return EarthSiteResponse.FAIL().message("删除失败");
    }


    @Log("sensorsType管理：删除对应的传感器类型")
    @DeleteMapping("completelyDelete")
    public EarthSiteResponse completelyDelete(String id) {
        if (sensorTypeAdminService.deleteSensorsType(id))
            return EarthSiteResponse.SUCCESS().message("删除成功");
        else
            return EarthSiteResponse.FAIL().message("删除失败");
    }

    @Log("sensorsType管理：查询对应的传感器类型")
    @GetMapping("querySensorsTypeInfo")
    public EarthSiteResponse querySensorsType(QueryRequest queryRequest, AdminSensorType adminSensorType) {
        IPage<AdminSensorType> adminSensorTypeIPage = sensorTypeAdminService.querySensorsTypeInfo(queryRequest, adminSensorType);
        Map<String, Object> dataTable = getDataTable(adminSensorTypeIPage);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    @Log("sensorsType管理：进行增加或者更新")
    @PostMapping("saveOrUpdate")
    public EarthSiteResponse addOrUpdateSensorsType(AdminSensorType adminSensorType) {
        boolean b = sensorTypeAdminService.saveOrUpdate(adminSensorType);
        return EarthSiteResponse.SUCCESS().data(b);
    }

    @Log("sensorsType管理：批量删除")
    @DeleteMapping("BatchDelete/{actionIds}")
    public EarthSiteResponse deleteBatchSensorsType(@NotBlank(message = "{required}") @PathVariable String actionIds) {
        try {
            //通过逗号进行分割为数组,循环输出进行删除
            String[] ids = actionIds.split(StringPool.COMMA);
            Arrays.stream(ids).forEach(id -> this.sensorTypeAdminService.deleteSensorsTypeByid(id));
        } catch (Exception e) {
            message = "批量删除失败";
            log.error(message, e);
            try {
                throw new EarthSiteException(message);
            } catch (EarthSiteException earthSiteException) {
                earthSiteException.printStackTrace();
            }
        }
        return EarthSiteResponse.SUCCESS().message("批量删除用户成功");
    }

    // @Log("报警管理:通过id进行还原信息")
    @PostMapping("restoreById")
    public EarthSiteResponse restoreSensors(String id) {
        //System.out.println("hahahahahah"+id);
        if (sensorTypeAdminService.restoreSensors(id)) {
            return EarthSiteResponse.SUCCESS().message("还原成功");
        }
        return EarthSiteResponse.FAIL().message("还原失败");
    }
}
