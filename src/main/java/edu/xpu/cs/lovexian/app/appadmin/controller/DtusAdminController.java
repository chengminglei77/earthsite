package edu.xpu.cs.lovexian.app.appadmin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtus;
import edu.xpu.cs.lovexian.app.appadmin.service.IDtusAdminService;
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
 *  Controller
 *
 * @author xpu
 * @date 2020-09-01 21:29:32
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/admin/dtus/")
public class DtusAdminController extends BaseController {
    private String message;
    @Autowired
    private IDtusAdminService dtusAdminService;

    @GetMapping("list")
    public EarthSiteResponse dtusList(QueryRequest request, AdminDtus adminDtus) {
        Map<String, Object> dataTable = getDataTable(this.dtusAdminService.findDtuss(request, adminDtus));
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    @Log("dtu管理:显示未删除的dtu的信息(status为0的信息)")
    @GetMapping("listByTypeId")
    public EarthSiteResponse getAllinfoByTypeId(QueryRequest request, AdminDtus adminDtus) {
        IPage<AdminDtus> dtuInfos = dtusAdminService.findDtusByTypeId(request, adminDtus);
        System.out.println(dtuInfos);
        Map<String, Object> dataTable = getDataTable(dtuInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }


    @Log("dtu管理:通过id进行删除信息")
    @DeleteMapping("deleteById")
    public EarthSiteResponse deleteDtu(String id) {
        if(dtusAdminService.deleteDtus(id)){
            return EarthSiteResponse.SUCCESS().message("删除成功");
        }
        return EarthSiteResponse.FAIL().message("删除失败");
    }


    @Log("dtu管理:批量删除信息")
    @DeleteMapping("BatchDelete/{actionIds}")
    public EarthSiteResponse deleteBatchDtus(@NotBlank(message = "{required}") @PathVariable String actionIds)
            throws EarthSiteException {
        try{
            //通过逗号进行分割为数组,循环输出进行删除
            String[] ids =actionIds.split(StringPool.COMMA);
            Arrays.stream(ids).forEach(id->this.dtusAdminService.deleteDtus(id));
        }catch (Exception e){
            message = "批量删除用户失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS().message("批量删除用户成功");
    }


    /**
     * 搜索(查询)dtu相关信息
     *
     * @param request
     * @param adminDtus
     * @return
     */
    @Log("dtu管理:查询相关信息")
    @GetMapping("queryDtuInfo")
    public EarthSiteResponse queryDtu(QueryRequest request, AdminDtus adminDtus) {
        IPage<AdminDtus> dtuInfos = dtusAdminService.queryDtuInfo(request, adminDtus);
        Map<String, Object> dataTable = getDataTable(dtuInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }



    @Log("dtu管理:保存更新(增加)相关信息")
    @PostMapping("saveOrUpdate")
    public EarthSiteResponse addOrUpdateDtus (AdminDtus adminDtus){
        System.out.println("=========================进入dtu添加功能========================");
        //String currentUserName = getCurrentUser();//得到当前用户名
        Date date = new Date();
        if (StringUtils.isEmpty(adminDtus.getId())) {//如果当前id不为空
            //adminDtus.setDtuName(currentUserName);//设置dtu的名称为当前用户名
            adminDtus.setCreatedAt(date);//设置dtu的创建时间
            adminDtus.setStatus(StatusEnum.NORMAL_STATE.getCode());//在前端显示该信息
        }
        adminDtus.setUpdatedAt(date);//设置dtu最后更新时间

        //保存或更新dtu信息
        boolean actOper = dtusAdminService.saveOrUpdate(adminDtus);
        return EarthSiteResponse.SUCCESS().data(actOper);
    }



    @Log("dtu管理:彻底删除信息")
    @DeleteMapping("completelyDelete")
    public EarthSiteResponse CompletelyDelete(String id) {
        if(dtusAdminService.completelyDeleteDtus(id)){
            return EarthSiteResponse.SUCCESS().message("彻底删除成功");
        }
        return EarthSiteResponse.FAIL().message("彻底删除失败");
    }


    //info类专用，其他类删除
//    @PostMapping("saveOrUpdate")
//    public EarthSiteResponse addOrUpdateDtus (AdminDtus adminDtus){
//
//    }
}
