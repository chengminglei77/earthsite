package edu.xpu.cs.lovexian.systemadmin.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import edu.xpu.cs.lovexian.common.annotation.ControllerEndpoint;
import edu.xpu.cs.lovexian.common.annotation.Log;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.common.exception.EarthSiteException;
import edu.xpu.cs.lovexian.systemadmin.domain.SysRole;
import edu.xpu.cs.lovexian.systemadmin.domain.SysRoleMenu;
import edu.xpu.cs.lovexian.systemadmin.service.ISysRoleMenuService;
import edu.xpu.cs.lovexian.systemadmin.service.ISysRoleService;
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
import java.util.stream.Collectors;

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
@RequestMapping("/system/role")
public class SysRoleController  extends BaseController {

    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private ISysRoleMenuService iSysRoleMenuService;

    private String message;

    @GetMapping("/list")
    @ControllerEndpoint(exceptionMessage = "获取角色列表失败")
    public EarthSiteResponse roleList(SysRole role, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.iSysRoleService.findRoles(role, request));
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    @GetMapping("/listAndMenu")
    @ControllerEndpoint(exceptionMessage = "获取角色列表失败")
    public EarthSiteResponse roleListAndMenus(SysRole role, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.iSysRoleService.findRolesAndMenus(role, request));
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }


    @GetMapping("/check/{roleName}")
    public boolean checkRoleName(@NotBlank(message = "{required}") @PathVariable String roleName) {
        SysRole result = this.iSysRoleService.findByName(roleName);
        return result == null;
    }

    @GetMapping("/menu/{roleId}")
    public List<String> getRoleMenus(@NotBlank(message = "{required}") @PathVariable String roleId) {
        List<SysRoleMenu> list = this.iSysRoleMenuService.getRoleMenusByRoleId(roleId);
        return list.stream().map(roleMenu -> String.valueOf(roleMenu.getMenuId())).collect(Collectors.toList());
    }

    @Log("新增角色")
    @PostMapping
    @RequiresPermissions("role:add")
    public EarthSiteResponse addRole(@Valid SysRole role) throws EarthSiteException {
        try {
            this.iSysRoleService.createRole(role);
        } catch (Exception e) {
            message = "新增角色失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @Log("删除角色")
    @DeleteMapping("/delete/{roleIds}")
    @RequiresPermissions("role:delete")
    public EarthSiteResponse deleteRoles(@NotBlank(message = "{required}") @PathVariable String roleIds) throws EarthSiteException {
        try {
            String[] ids = roleIds.split(StringPool.COMMA);
            this.iSysRoleService.deleteRoles(ids);
        } catch (Exception e) {
            message = "删除角色失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @Log("修改角色")
    @PutMapping
    @RequiresPermissions("role:update")
    public EarthSiteResponse updateRole(SysRole role) throws EarthSiteException {
        try {
            this.iSysRoleService.updateRole(role);
        } catch (Exception e) {
            message = "修改角色失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @GetMapping("/excel")
    @RequiresPermissions("role:export")
    public EarthSiteResponse export(QueryRequest queryRequest, SysRole role, HttpServletResponse response) throws EarthSiteException {
        try {
            List<SysRole> roles = this.iSysRoleService.findRoles(role, queryRequest).getRecords();
            ExcelKit.$Export(SysRole.class, response).downXlsx(roles, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }
}
