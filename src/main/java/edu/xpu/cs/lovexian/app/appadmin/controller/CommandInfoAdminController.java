package edu.xpu.cs.lovexian.app.appadmin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCommandInfo;
import edu.xpu.cs.lovexian.app.appadmin.service.ICommandInfoAdminService;
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
 * @date 2020-09-21 19:42:21
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/admin/commandInfo/")
public class CommandInfoAdminController extends BaseController {
    private String message;
    @Autowired
    private ICommandInfoAdminService commandInfoAdminService;


    @GetMapping("list")
    public EarthSiteResponse commandInfoList(QueryRequest request, AdminCommandInfo adminCommandInfo) {
        Map<String, Object> dataTable = getDataTable(this.commandInfoAdminService.findCommandInfos(request, adminCommandInfo));
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    //@Log("报警管理:显示未删除的信息")
    //显示出列表++++查询
    @GetMapping("listByTypeId")
    public EarthSiteResponse getAllinfoByTypeId(QueryRequest request, AdminCommandInfo adminCommandInfo) {
        IPage<AdminCommandInfo> commandInfos =commandInfoAdminService.findCommandInfosByTypeId(request, adminCommandInfo);
        Map<String, Object> dataTable = getDataTable(commandInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    //info类专用，其他类删除
    @PostMapping("saveOrUpdate")
    public EarthSiteResponse addOrUpdateCommandInfo(AdminCommandInfo adminCommandInfo) {

        Date date = new Date();
        if (StringUtils.isEmpty(adminCommandInfo.getId())) {
//            adminCommandInfo.setCommand(date);//创建的时间
            adminCommandInfo.setCmdStatus(StatusEnum.NORMAL_STATE.getCode());//选择状态
        }
        //adminCommandInfo.setUpdatedAt(date);//设置最后的更新时间

        //保存或更新dtu信息
        boolean actOper = commandInfoAdminService.saveOrUpdate(adminCommandInfo);
        return EarthSiteResponse.SUCCESS().data(actOper);
    }
    /**
     * 搜索(查询)dtu相关信息
     *
     * @param request
     * @param adminCommandInfo
     * @return
     */
    @Log("dtu管理:查询相关信息")
    @GetMapping("queryDtuInfo")
    public EarthSiteResponse queryDtu(QueryRequest request, AdminCommandInfo adminCommandInfo) {
        IPage<AdminCommandInfo> commandInfos = commandInfoAdminService.queryCommandInfos(request, adminCommandInfo);
        Map<String, Object> dataTable = getDataTable(commandInfos);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }
    //@Log("报警管理:批量删除信息")
    @DeleteMapping("BatchDelete/{actionIds}")
    public EarthSiteResponse deleteBatchCommands(@NotBlank(message = "{required}") @PathVariable String actionIds)
            throws EarthSiteException {
        try{
            //通过逗号进行分割为数组,循环输出进行删除
            String ids[]=actionIds.split(StringPool.COMMA);
            Arrays.stream(ids).forEach(id->this.commandInfoAdminService.deleteCommands(id));
        }catch (Exception e){
            message = "批量删除用户失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS().message("批量删除用户成功");
    }
    // @Log("报警管理:通过id进行删除信息")
    @DeleteMapping("deleteById")
    public EarthSiteResponse deleteCommand(String id) {
        if(commandInfoAdminService.deleteCommands(id)){
            return EarthSiteResponse.SUCCESS().message("删除成功");
        }
        return EarthSiteResponse.FAIL().message("删除失败");
    }





    @Log("报警管理:彻底删除信息")
    @DeleteMapping("completelyDelete")
    public EarthSiteResponse CompletelyDelete(String id) {
        if(commandInfoAdminService.completelyDeleteCommandInfo(id)){
            return EarthSiteResponse.SUCCESS().message("彻底删除成功");
        }
        return EarthSiteResponse.FAIL().message("彻底删除失败");
    }




    // @Log("报警管理:通过id进行还原信息")
    @GetMapping("restoreById")
    public EarthSiteResponse restoreCommand(String id) {
        if(commandInfoAdminService.restoreCommands(id)){
            return EarthSiteResponse.SUCCESS().message("还原成功");
        }
        return EarthSiteResponse.FAIL().message("还原失败");
    }

}
