package edu.xpu.cs.lovexian.systemadmin.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import edu.xpu.cs.lovexian.common.annotation.Log;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.router.MenuTree;
import edu.xpu.cs.lovexian.common.exception.EarthSiteException;
import edu.xpu.cs.lovexian.systemadmin.domain.SysMenu;
import edu.xpu.cs.lovexian.systemadmin.manager.UserManager;
import edu.xpu.cs.lovexian.systemadmin.service.ISysMenuService;
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
@RequestMapping("/system/menu")
public class SysMenuController  extends BaseController {
    private String message;

    @Autowired
    private UserManager userManager;
    @Autowired
    private ISysMenuService iSysMenuService;

    @GetMapping("/menu")
    public EarthSiteResponse getUserMenus() throws EarthSiteException {
        String currentUserName = getCurrentUser();
//        if (!StringUtils.equalsIgnoreCase(username,currentUserName)){
//            throw new LoveXianException("您无权获取别人的菜单");
//        }
        MenuTree<SysMenu> userMenus = this.iSysMenuService.findUserMenuByName(currentUserName);
        return EarthSiteResponse.SUCCESS().data(userMenus.getList()).message("构建菜单成功");
    }

    @GetMapping("/tree")
    public EarthSiteResponse getMenuTree(SysMenu menu) throws EarthSiteException {
        MenuTree<SysMenu> userMenus = this.iSysMenuService.genMenusTree(menu);
        return EarthSiteResponse.SUCCESS().data(userMenus.getList()).message("构建菜单成功");
    }

    @GetMapping
    @RequiresPermissions("menu:view")
    public Map<String, Object> menuList(SysMenu menu) {
        return this.iSysMenuService.findMenus(menu);
    }

    @Log("新增菜单/按钮")
    @PostMapping
    @RequiresPermissions("menu:add")
    public EarthSiteResponse addMenu(@Valid SysMenu menu) throws EarthSiteException {
        try {
            this.iSysMenuService.createMenu(menu);
        } catch (Exception e) {
            message = "新增菜单/按钮失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @Log("删除菜单/按钮")
    @DeleteMapping("/{menuIds}")
    @RequiresPermissions("menu:delete")
    public EarthSiteResponse deleteMenus(@NotBlank(message = "{required}") @PathVariable String menuIds) throws EarthSiteException {
        try {
            String[] ids = menuIds.split(StringPool.COMMA);
            this.iSysMenuService.deleteMeuns(ids);
        } catch (Exception e) {
            message = "删除菜单/按钮失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @Log("修改菜单/按钮")
    @PutMapping
    @RequiresPermissions("menu:update")
    public EarthSiteResponse updateMenu(@Valid SysMenu menu) throws EarthSiteException {
        try {
            this.iSysMenuService.updateMenu(menu);
        } catch (Exception e) {
            message = "修改菜单/按钮失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @GetMapping("/excel")
    @RequiresPermissions("menu:export")
    public EarthSiteResponse export(SysMenu menu, HttpServletResponse response) throws EarthSiteException {
        try {
            List<SysMenu> menus = this.iSysMenuService.findMenuList(menu);
            ExcelKit.$Export(SysMenu.class, response).downXlsx(menus, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }
}
