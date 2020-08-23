package edu.xpu.cs.lovexian.appadmin.controller;

import edu.xpu.cs.lovexian.common.annotation.ControllerEndpoint;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import ${basePackage}.${entityPackage}.Admin${className};
import ${basePackage}.${servicePackage}.I${className}AdminService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * ${tableComment} Controller
 *
 * @author ${author}
 * @date ${date}
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/admin/${className?uncap_first}/")
public class ${className}AdminController extends BaseController {
    private String message;
    @Autowired
    private I${className}AdminService ${className?uncap_first}AdminService;


    @GetMapping("list")
    public EarthSiteResponse ${className?uncap_first}List(QueryRequest request, Admin${className} admin${className}) {
        Map<String, Object> dataTable = getDataTable(this.${className?uncap_first}AdminService.find${className}s(request, admin${className}));
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    //info类专用，其他类删除
    @PostMapping("saveOrUpdate")
    public EarthSiteResponse addOrUpdate${className} (Admin${className} admin${className}){

    }
}
