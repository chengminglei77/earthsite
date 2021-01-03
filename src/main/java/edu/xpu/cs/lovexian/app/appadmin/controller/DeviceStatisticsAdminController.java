package edu.xpu.cs.lovexian.app.appadmin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDeviceStatistics;
import edu.xpu.cs.lovexian.app.appadmin.service.IDeviceStatisticsAdminService;
import edu.xpu.cs.lovexian.app.appadmin.service.IDtusAdminService;
import edu.xpu.cs.lovexian.common.annotation.Log;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//import org.springframework.stereotype.Service;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.validation.annotation.Validated;

/**
 * @author czy
 * @create 2020-10-25-17:59
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/admin/DeviceStatistics/")
public class DeviceStatisticsAdminController extends BaseController {

    @Autowired
    private IDeviceStatisticsAdminService iDeviceStatisticsAdminService;

    @Autowired
    private IDtusAdminService dtusAdminService;

    @Log("设备统计管理:显示对应的传感器/dtu/网关对应的信息")
    @GetMapping("listByTypeId")
    public EarthSiteResponse getInfoByTypeId(QueryRequest request, AdminDeviceStatistics adminDeviceStatistics) {
        IPage<AdminDeviceStatistics> StatisticsInfos = iDeviceStatisticsAdminService.findDeviceStatisticsByTypeId(request, adminDeviceStatistics);
        Map<String, Object> dataTable = getDataTable(StatisticsInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    @Log("设备统计管理:删除对应的信息")
    @DeleteMapping("deleteById")
    public EarthSiteResponse deleteById(String id) {
        boolean b = iDeviceStatisticsAdminService.deleteDevice(id);
        if (b) {
            return EarthSiteResponse.SUCCESS().message("删除成功");
        }
        return EarthSiteResponse.FAIL().message("删除失败");
    }

}
