package edu.xpu.cs.lovexian.systemadmin.manager;

import edu.xpu.cs.lovexian.common.service.CacheService;
import edu.xpu.cs.lovexian.common.utils.LoveXianUtil;
import edu.xpu.cs.lovexian.systemadmin.domain.SysMenu;
import edu.xpu.cs.lovexian.systemadmin.domain.SysRole;
import edu.xpu.cs.lovexian.systemadmin.domain.SysUser;
import edu.xpu.cs.lovexian.systemadmin.domain.SysUserConfig;
import edu.xpu.cs.lovexian.systemadmin.service.ISysMenuService;
import edu.xpu.cs.lovexian.systemadmin.service.ISysRoleService;
import edu.xpu.cs.lovexian.systemadmin.service.ISysUserConfigService;
import edu.xpu.cs.lovexian.systemadmin.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
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
 * 封装一些和 User相关的业务操作
 */
@Service
public class UserManager {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private ISysMenuService iSysMenuService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ISysUserConfigService iSysUserConfigService;


    /**
     * 通过用户名获取用户基本信息
     *
     * @param username 用户名
     * @return 用户基本信息
     */
    public SysUser getUser(String username) {
        return LoveXianUtil.selectCacheByTemplate(
                () -> this.cacheService.getUser(username),
                () -> this.iSysUserService.findDetailByName(username));
    }

    /**
     * 通过用户名获取用户角色集合
     *
     * @param username 用户名
     * @return 角色集合
     */
    public Set<String> getUserRoles(String username) {
        List<SysRole> roleList = LoveXianUtil.selectCacheByTemplate(
                () -> this.cacheService.getRoles(username),
                () -> this.iSysRoleService.findUserRole(username));
        return roleList.stream().map(SysRole::getRoleName).collect(Collectors.toSet());
    }

    /**
     * 通过用户名获取用户权限集合
     *
     * @param username 用户名
     * @return 权限集合
     */
    public Set<String> getUserPermissions(String username) {
        List<SysMenu> permissionList = LoveXianUtil.selectCacheByTemplate(
                () -> this.cacheService.getPermissions(username),
                () -> this.iSysMenuService.findUserPermissions(username));
        return permissionList.stream().map(SysMenu::getPerms).collect(Collectors.toSet());
    }


    /**
     * 通过用户 ID获取前端系统个性化配置
     *
     * @param userId 用户 ID
     * @return 前端系统个性化配置
     */
    public SysUserConfig getUserConfig(String userId) {
        return LoveXianUtil.selectCacheByTemplate(
                () -> this.cacheService.getUserConfig(userId),
                () -> this.iSysUserConfigService.findByUserId(userId));
    }

    /**
     * 将用户相关信息添加到 Redis缓存中
     *
     * @param user user
     */
    public void loadUserRedisCache(SysUser user) throws Exception {
        // 缓存用户
        cacheService.saveUser(user.getUsername());
        // 缓存用户角色
        cacheService.saveRoles(user.getUsername());
        // 缓存用户权限
        cacheService.savePermissions(user.getUsername());
        // 缓存用户个性化配置
        cacheService.saveUserConfigs(String.valueOf(user.getUserId()));
    }

    /**
     * 将用户角色和权限添加到 Redis缓存中
     *
     * @param userIds userIds
     */
    public void loadUserPermissionRoleRedisCache(List<String> userIds) throws Exception {
        for (String userId : userIds) {
            SysUser user = iSysUserService.getById(userId);
            // 缓存用户角色
            cacheService.saveRoles(user.getUsername());
            // 缓存用户权限
            cacheService.savePermissions(user.getUsername());
        }
    }

    /**
     * 通过用户 id集合批量删除用户 Redis缓存
     *
     * @param userIds userIds
     */
    public void deleteUserRedisCache(String... userIds) throws Exception {
        for (String userId : userIds) {
            SysUser user = iSysUserService.getById(userId);
            if (user != null) {
                cacheService.deleteUser(user.getUsername());
                cacheService.deleteRoles(user.getUsername());
                cacheService.deletePermissions(user.getUsername());
            }
            cacheService.deleteUserConfigs(userId);
        }
    }

}
