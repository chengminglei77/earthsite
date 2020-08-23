package edu.xpu.cs.lovexian.systemadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.xpu.cs.lovexian.common.domain.router.MenuTree;
import edu.xpu.cs.lovexian.systemadmin.domain.SysMenu;

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
public interface ISysMenuService extends IService<SysMenu> {
    List<SysMenu> findUserPermissions(String username);

    List<SysMenu> findUserMenus(String username);

    /**
     * 查找用户菜单集合
     *
     * @param username 用户名
     * @return 用户菜单集合
     */
    MenuTree<SysMenu> findUserMenuByName(String username);

    Map<String, Object> findMenus(SysMenu menu);

    MenuTree<SysMenu> genMenusTree(SysMenu menu);

    List<SysMenu> findMenuList(SysMenu menu);

    void createMenu(SysMenu menu);

    void updateMenu(SysMenu menu) throws Exception;

    /**
     * 递归删除菜单/按钮
     *
     * @param menuIds menuIds
     */
    void deleteMeuns(String[] menuIds) throws Exception;
}
