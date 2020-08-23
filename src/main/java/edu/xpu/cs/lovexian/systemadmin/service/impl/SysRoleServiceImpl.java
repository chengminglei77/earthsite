package edu.xpu.cs.lovexian.systemadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.common.domain.EarthSiteConstant;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.common.utils.SortUtil;
import edu.xpu.cs.lovexian.systemadmin.domain.SysRole;
import edu.xpu.cs.lovexian.systemadmin.domain.SysRoleMenu;
import edu.xpu.cs.lovexian.systemadmin.manager.UserManager;
import edu.xpu.cs.lovexian.systemadmin.mapper.SysRoleMapper;
import edu.xpu.cs.lovexian.systemadmin.mapper.SysRoleMenuMapper;
import edu.xpu.cs.lovexian.systemadmin.service.ISysRoleMenuService;
import edu.xpu.cs.lovexian.systemadmin.service.ISysRoleService;
import edu.xpu.cs.lovexian.systemadmin.service.ISysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
@Service("iSysRoleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;
    @Autowired
    private ISysRoleMenuService iSysRoleMenuService;
    @Autowired
    private UserManager userManager;

    @Override
    public IPage<SysRole> findRolesAndMenus(SysRole role, QueryRequest request) {
        Page<SysRole> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", EarthSiteConstant.ORDER_DESC, false);
        return this.baseMapper.findRolAndMenuPage(page, role);
    }

    @Override
    public IPage<SysRole> findRoles(SysRole role, QueryRequest request) {
        try {
            LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();

            if (StringUtils.isNotBlank(role.getRoleName())) {
                queryWrapper.eq(SysRole::getRoleName, role.getRoleName());
            }
            if (StringUtils.isNotBlank(role.getCreateTimeFrom()) && StringUtils.isNotBlank(role.getCreateTimeTo())) {
                queryWrapper
                        .ge(SysRole::getCreateTime, role.getCreateTimeFrom())
                        .le(SysRole::getCreateTime, role.getCreateTimeTo());
            }
            Page<SysRole> page = new Page<>();
            SortUtil.handlePageSort(request, page, true);
            return this.page(page,queryWrapper);
        } catch (Exception e) {
            log.error("获取角色信息失败", e);
            return null;
        }
    }

    @Override
    public List<SysRole> findUserRole(String userName) {
        return baseMapper.findUserRole(userName);
    }

    @Override
    public SysRole findByName(String roleName) {
        return baseMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleName, roleName));
    }

    @Override
    public void createRole(SysRole role) {
        role.setCreateTime(new Date());
        this.save(role);

        String[] menuIds = role.getMenuId().split(StringPool.COMMA);
        setRoleMenus(role, menuIds);
    }

    @Override
    public void deleteRoles(String[] roleIds) throws Exception {
        // 查找这些角色关联了那些用户
        List<String> userIds = this.iSysUserRoleService.findUserIdsByRoleId(roleIds);

        List<String> list = Arrays.asList(roleIds);

        baseMapper.deleteBatchIds(list);

        this.iSysRoleMenuService.deleteRoleMenusByRoleId(roleIds);
        this.iSysUserRoleService.deleteUserRolesByRoleId(roleIds);

        // 重新将这些用户的角色和权限缓存到 Redis中
        this.userManager.loadUserPermissionRoleRedisCache(userIds);

    }

    @Override
    public void updateRole(SysRole role) throws Exception {
        // 查找这些角色关联了那些用户
        String[] roleId = {String.valueOf(role.getRoleId())};
        List<String> userIds = this.iSysUserRoleService.findUserIdsByRoleId(roleId);

        role.setUpdateTime(new Date());
        baseMapper.updateById(role);

        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, role.getRoleId()));
        String[] menuIds = new String[0];
        if(StringUtils.isNotEmpty(role.getMenuId())){
            menuIds = role.getMenuId().split(StringPool.COMMA);
            setRoleMenus(role, menuIds);
        }else{
            setRoleMenus(role, menuIds);
        }


        // 重新将这些用户的角色和权限缓存到 Redis中
        this.userManager.loadUserPermissionRoleRedisCache(userIds);
    }

    private void setRoleMenus(SysRole role, String[] menuIds) {
        Arrays.stream(menuIds).forEach(menuId -> {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setMenuId(Long.valueOf(menuId));
            rm.setRoleId(role.getRoleId());
            this.sysRoleMenuMapper.insert(rm);
        });
    }
}
