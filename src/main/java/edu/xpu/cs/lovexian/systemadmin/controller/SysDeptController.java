package edu.xpu.cs.lovexian.systemadmin.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import edu.xpu.cs.lovexian.common.annotation.ControllerEndpoint;
import edu.xpu.cs.lovexian.common.annotation.Log;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.common.domain.router.DeptTree;
import edu.xpu.cs.lovexian.common.exception.EarthSiteException;
import edu.xpu.cs.lovexian.systemadmin.domain.SysDept;
import edu.xpu.cs.lovexian.systemadmin.service.ISysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**                                                                                ____________________
      _                _                                                           < 神兽护体，永无bug! >
    | |__  _   _  ___| |__   ___ _ __   __ _ _ __   ___ _ __   __ _                --------------------
   | '_ \| | | |/ __| '_ \ / _ \ '_ \ / _` | '_ \ / _ \ '_ \ / _` |                       \   ^__^
  | | | | |_| | (__| | | |  __/ | | | (_| | |_) |  __/ | | | (_| |                        \  (oo)\_______
 |_| |_|\__,_|\___|_| |_|\___|_| |_|\__, | .__/ \___|_| |_|\__, |                           (__)\       )\/\
                                   |___/|_|                |___/                                ||----w |
                                                                                                ||     ||
 * @author huchengpeng
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {
    private String message;

    @Autowired
    private ISysDeptService iSysDeptService;

    @GetMapping
    public Map<String, Object> deptList(QueryRequest request, SysDept dept) {
        return this.iSysDeptService.findDepts(request, dept);
    }

    @GetMapping("/select/tree")
    @ControllerEndpoint(exceptionMessage = "获取部门树失败")
    public List<DeptTree<SysDept>> getDeptTree() throws EarthSiteException {
        return this.iSysDeptService.findDepts();
    }

    @GetMapping("/tree")
    @ControllerEndpoint(exceptionMessage = "获取部门树失败")
    public EarthSiteResponse getDeptTree(SysDept dept) throws EarthSiteException {
        List<DeptTree<SysDept>> depts = this.iSysDeptService.findDepts(dept);
        return EarthSiteResponse.SUCCESS().data(depts);
    }

    @Log("新增部门")
    @PostMapping
    @RequiresPermissions("dept:add")
    public EarthSiteResponse addDept(@Valid SysDept dept) throws EarthSiteException {
        try {
            this.iSysDeptService.createDept(dept);
        } catch (Exception e) {
            message = "新增部门失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @Log("删除部门")
    @DeleteMapping("/{deptIds}")
    @RequiresPermissions("dept:delete")
    public EarthSiteResponse deleteDepts(@NotBlank(message = "{required}") @PathVariable String deptIds) throws EarthSiteException {
        try {
            String[] ids = deptIds.split(StringPool.COMMA);
            this.iSysDeptService.deleteDepts(ids);
        } catch (Exception e) {
            message = "删除部门失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @Log("修改部门")
    @PutMapping
    @RequiresPermissions("dept:update")
    public EarthSiteResponse updateDept(@Valid SysDept dept) throws EarthSiteException {
        try {
            this.iSysDeptService.updateDept(dept);
        } catch (Exception e) {
            message = "修改部门失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @GetMapping("excel")
    @RequiresPermissions("dept:export")
    public EarthSiteResponse export(SysDept dept, QueryRequest request, HttpServletResponse response) throws EarthSiteException {
        try {
            List<SysDept> depts = this.iSysDeptService.findDepts(dept, request);
            ExcelKit.$Export(SysDept.class, response).downXlsx(depts, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }
}
