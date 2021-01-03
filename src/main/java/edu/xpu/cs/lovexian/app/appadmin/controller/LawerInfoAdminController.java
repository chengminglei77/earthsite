package edu.xpu.cs.lovexian.app.appadmin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;

import edu.xpu.cs.lovexian.app.appadmin.entity.AdminLawerInfo;
import edu.xpu.cs.lovexian.app.appadmin.service.ILawerInfoAdminService;
import edu.xpu.cs.lovexian.app.constant.Constant;
import edu.xpu.cs.lovexian.common.annotation.Log;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.common.exception.EarthSiteException;
import edu.xpu.cs.lovexian.common.utils.DateUtil;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 律师信息表 Controller
 *
 * @author xpu
 * @date 2019-12-25 15:25:48
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/admin/lawerInfo/")
public class LawerInfoAdminController extends BaseController {
    private String message;
    @Autowired
    private ILawerInfoAdminService lawerInfoAdminService;


    @GetMapping("list")
    public EarthSiteResponse lawerInfoList(QueryRequest request, AdminLawerInfo adminLawerInfo) {
        Map<String, Object> dataTable = getDataTable(this.lawerInfoAdminService.findLawerInfos(request, adminLawerInfo));
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }


    @PostMapping("saveOrUpdate")
    public EarthSiteResponse addOrUpdateLawerInfo(AdminLawerInfo adminLawerInfo) {
        System.out.println("=========================进入lawer添加功能========================");
        String currentUserName = getCurrentUser();
        Date date = new Date();
        if (StringUtils.isEmpty(adminLawerInfo.getId())) {
            adminLawerInfo.setCreatorName(currentUserName);
            adminLawerInfo.setCreateTime(date);
            adminLawerInfo.setDelState(0);
            adminLawerInfo.setUpdaterName(currentUserName);

        }
        adminLawerInfo.setUpdateTime(date);

        //保存或更新文章信息
        boolean actOper = lawerInfoAdminService.saveOrUpdate(adminLawerInfo);
        boolean actCheckOper = false;
        //根据checkState判断是草稿还是待审核的信息，需要往审核表存储
        if (adminLawerInfo.getCheckState().equals(Constant.CheckStateEnum.draft.getCode())) {
            System.out.println("这是保存草稿的方法");
        } else if (adminLawerInfo.getCheckState().equals(Constant.CheckStateEnum.uncheck.getCode())) {
            //发布审核

        }
        return actOper && actCheckOper ? EarthSiteResponse.SUCCESS()
                : EarthSiteResponse.SUCCESS().message(adminLawerInfo.getCheckState().equals(Constant.CheckStateEnum.draft.getCode())
                ? "保存草稿出问题了哦！" : "发布工作信息出问题了哦！");

    }

    @DeleteMapping("deleteById")
    public EarthSiteResponse deleteLawerInfo(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("lawer_id", id);
        if (lawerInfoAdminService.deleteLawerInfo(id)) {
            return EarthSiteResponse.SUCCESS().message("删除成功");
        }
        return EarthSiteResponse.FAIL().message("删除失败");
    }

    /**
     * 查找不同类别的信息
     *
     * @param request
     * @param adminLawerInfo
     * @return
     */
    @GetMapping("listByTypeId")
    public EarthSiteResponse getAllinfoByTypeId(QueryRequest request, AdminLawerInfo adminLawerInfo) {
        IPage<AdminLawerInfo> lawerInfos = lawerInfoAdminService.findLawerInfosByTypeId(request, adminLawerInfo);
        lawerInfos.getRecords().forEach((data) -> {
            String dateFormat = DateUtil.getDateFormat(data.getWorkTime(), DateUtil.YMD);
            data.setWorkTime(DateUtil.StrToDate(dateFormat, DateUtil.YMD));
        });
        Map<String, Object> dataTable = getDataTable(lawerInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }


    /**
     * 搜索相关信息
     *
     * @param request
     * @param adminLawerInfo
     * @return
     */
    @GetMapping("queryLawerInfo")
    public EarthSiteResponse queryLawerInfo(QueryRequest request, AdminLawerInfo adminLawerInfo) {
        IPage<AdminLawerInfo> lawerInfos = lawerInfoAdminService.queryLawerInfo(request, adminLawerInfo);
        Map<String, Object> dataTable = getDataTable(lawerInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }


    /**
     * 更新信息的展示状态
     *
     * @return
     */
    @Log("更新爱公平的置顶和展示状态")
    @PostMapping("updateLawer")
    public EarthSiteResponse updateIsShow(AdminLawerInfo lawerInfo) {
        Boolean result = lawerInfoAdminService.updateById(lawerInfo);
        return result ? EarthSiteResponse.SUCCESS()
                : EarthSiteResponse.FAIL();
    }

    @DeleteMapping("BatchDelete/{actionIds}")
    public EarthSiteResponse deleteBatchLawer(@NotBlank(message = "{required}") @PathVariable String actionIds)
            throws EarthSiteException {
        try {
            String ids[] = actionIds.split(StringPool.COMMA);
            Arrays.stream(ids).forEach(id -> this.lawerInfoAdminService.deleteBatchLawer(id));

        } catch (Exception e) {
            message = "批量删除用户失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS().message("批量删除用户成功");
    }

}
