package edu.xpu.cs.lovexian.systemadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.common.domain.EarthSiteConstant;
import edu.xpu.cs.lovexian.common.domain.Tree;
import edu.xpu.cs.lovexian.common.domain.router.MenuTree;
import edu.xpu.cs.lovexian.common.utils.TreeUtil;
import edu.xpu.cs.lovexian.systemadmin.domain.SysMenu;
import edu.xpu.cs.lovexian.systemadmin.manager.UserManager;
import edu.xpu.cs.lovexian.systemadmin.mapper.SysMenuMapper;
import edu.xpu.cs.lovexian.systemadmin.service.ISysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
@Service("iSysMenuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
    @Autowired
    private UserManager userManager;

    @Override
    public MenuTree<SysMenu> findUserMenuByName(String username) {
        List<SysMenu> menus = this.baseMapper.findUserMenus(username);
        List<MenuTree<SysMenu>> trees = this.convertMenus(menus);
        return TreeUtil.buildMenuTree(trees);
    }
    private List<MenuTree<SysMenu>> convertMenus(List<SysMenu> menus) {
        List<MenuTree<SysMenu>> trees = new ArrayList<>();
        menus.forEach(menu -> {
            MenuTree<SysMenu> tree = new MenuTree<>();
            tree.setId(String.valueOf(menu.getMenuId()));
            tree.setParentId(String.valueOf(menu.getParentId()));
            tree.setTitle(menu.getMenuName());
            tree.setIcon(menu.getIcon());
            tree.setJump(menu.getMenuPath());
            tree.setData(menu);
            trees.add(tree);
        });
        return trees;
    }

    @Override
    public List<SysMenu> findUserPermissions(String username) {
        return this.baseMapper.findUserPermissions(username);
    }

    @Override
    public List<SysMenu> findUserMenus(String username) {
        return this.baseMapper.findUserMenus(username);
    }


    @Override
    public MenuTree<SysMenu> genMenusTree(SysMenu menu) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(menu.getMenuName())) {
            queryWrapper.lambda().like(SysMenu::getMenuName, menu.getMenuName());
        }
        queryWrapper.lambda().orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> menus = this.baseMapper.selectList(queryWrapper);
        List<MenuTree<SysMenu>> trees = this.convertMenus(menus);

        return TreeUtil.buildMenuTree(trees);
    }

    @Override
    public Map<String, Object> findMenus(SysMenu menu) {
        Map<String, Object> result = new HashMap<>();
        try {
            LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
            findMenuCondition(queryWrapper, menu);
            List<SysMenu> menus = baseMapper.selectList(queryWrapper);

            List<Tree<SysMenu>> trees = new ArrayList<>();
            List<String> ids = new ArrayList<>();
            buildTrees(trees, menus, ids);

            result.put("ids", ids);
            if (StringUtils.equals(menu.getMenuType(), EarthSiteConstant.TYPE_BUTTON)) {
                result.put("rows", trees);
            } else {
                Tree<SysMenu> menuTree = TreeUtil.build(trees);
                result.put("rows", menuTree);
            }

            result.put("total", menus.size());
        } catch (NumberFormatException e) {
            log.error("查询菜单失败", e);
            result.put("rows", null);
            result.put("total", 0);
        }
        return result;
    }


    @Override
    public List<SysMenu> findMenuList(SysMenu menu) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        findMenuCondition(queryWrapper, menu);
        queryWrapper.orderByAsc(SysMenu::getMenuId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createMenu(SysMenu menu) {
        menu.setCreateTime(new Date());
        setMenu(menu);
        this.save(menu);
    }

    @Override
    @Transactional
    public void updateMenu(SysMenu menu) throws Exception {
        menu.setUpdateTime(new Date());
        setMenu(menu);
        baseMapper.updateById(menu);

        // 查找与这些菜单/按钮关联的用户
        List<String> userIds = this.baseMapper.findUserIdsByMenuId(String.valueOf(menu.getMenuId()));
        // 重新将这些用户的角色和权限缓存到 Redis中
        this.userManager.loadUserPermissionRoleRedisCache(userIds);
    }

    @Override
    @Transactional
    public void deleteMeuns(String[] menuIds) throws Exception {
        this.delete(Arrays.asList(menuIds));
        for (String menuId : menuIds) {
            // 查找与这些菜单/按钮关联的用户
            List<String> userIds = this.baseMapper.findUserIdsByMenuId(String.valueOf(menuId));
            // 重新将这些用户的角色和权限缓存到 Redis中
            this.userManager.loadUserPermissionRoleRedisCache(userIds);
        }
    }

    private void buildTrees(List<Tree<SysMenu>> trees, List<SysMenu> menus, List<String> ids) {
        menus.forEach(menu -> {
            ids.add(menu.getMenuId().toString());
            Tree<SysMenu> tree = new Tree<>();
            tree.setId(menu.getMenuId().toString());
            tree.setKey(tree.getId());
            tree.setParentId(menu.getParentId().toString());
            tree.setText(menu.getMenuName());
            tree.setTitle(tree.getText());
            tree.setIcon(menu.getIcon());
            tree.setComponent(menu.getComponent());
            tree.setCreateTime(menu.getCreateTime());
            tree.setModifyTime(menu.getUpdateTime());
            tree.setPath(menu.getMenuPath());
            tree.setOrder(menu.getOrderNum());
            tree.setPermission(menu.getPerms());
            tree.setType(menu.getMenuType());
            trees.add(tree);
        });
    }

    private void setMenu(SysMenu menu) {
        if (menu.getParentId() == null){
            menu.setParentId(0L);
        }
        if (SysMenu.TYPE_BUTTON.equals(menu.getMenuType())) {
            menu.setMenuPath(null);
            menu.setIcon(null);
            menu.setComponent(null);
        }
    }

    private void findMenuCondition(LambdaQueryWrapper<SysMenu> queryWrapper, SysMenu menu) {
        if (StringUtils.isNotBlank(menu.getMenuName())) {
            queryWrapper.eq(SysMenu::getMenuName, menu.getMenuName());
        }
        if (StringUtils.isNotBlank(menu.getMenuType())) {
            queryWrapper.eq(SysMenu::getMenuType, menu.getMenuType());
        }
        if (StringUtils.isNotBlank(menu.getCreateTimeFrom()) && StringUtils.isNotBlank(menu.getCreateTimeTo())) {
            queryWrapper
                    .ge(SysMenu::getCreateTime, menu.getCreateTimeFrom())
                    .le(SysMenu::getCreateTime, menu.getCreateTimeTo());
        }
    }


    private void delete(List<String> menuIds) {
        removeByIds(menuIds);

        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysMenu::getParentId, menuIds);
        List<SysMenu> menus = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(menus)) {
            List<String> menuIdList = new ArrayList<>();
            menus.forEach(m -> menuIdList.add(String.valueOf(m.getMenuId())));
            this.delete(menuIdList);
        }
    }
}
