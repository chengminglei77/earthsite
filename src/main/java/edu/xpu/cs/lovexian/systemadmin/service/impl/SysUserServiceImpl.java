package edu.xpu.cs.lovexian.systemadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.common.domain.EarthSiteConstant;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.common.service.CacheService;
import edu.xpu.cs.lovexian.common.utils.MD5Util;
import edu.xpu.cs.lovexian.common.utils.SortUtil;
import edu.xpu.cs.lovexian.systemadmin.domain.SysUser;
import edu.xpu.cs.lovexian.systemadmin.domain.SysUserRole;
import edu.xpu.cs.lovexian.systemadmin.manager.UserManager;
import edu.xpu.cs.lovexian.systemadmin.mapper.SysUserMapper;
import edu.xpu.cs.lovexian.systemadmin.mapper.SysUserRoleMapper;
import edu.xpu.cs.lovexian.systemadmin.service.ISysUserConfigService;
import edu.xpu.cs.lovexian.systemadmin.service.ISysUserRoleService;
import edu.xpu.cs.lovexian.systemadmin.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
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
@Service("iSysUserService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private ISysUserConfigService iSysUserConfigService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private ISysUserRoleService iSysUserRoleService;
    @Autowired
    private UserManager userManager;

    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    public SysUser findByName(String username) {
        return baseMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }

    @Override
    public SysUser findDetailByName(String username) {
        return sysUserMapper.findDetail(username);
    }

    @Override
    public IPage<SysUser> findUserDetail(SysUser user, QueryRequest request) {
        try {
            Page<SysUser> page = new Page<>();
            SortUtil.handlePageSort(request, page, "userId", EarthSiteConstant.ORDER_ASC, false);
            return this.baseMapper.findUserDetail(page, user);
        } catch (Exception e) {
            log.error("查询用户异常", e);
            return null;
        }
    }

    @Override
    @Transactional
    public void updateLoginTime(String username) throws Exception {
        SysUser user = new SysUser();
        user.setLastLoginTime(new Date());

        this.baseMapper.update(user, new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));

        // 重新将用户信息加载到 redis中
        cacheService.saveUser(username);
    }

    @Override
    @Transactional
    public void createUser(SysUser user) throws Exception {
        // 创建用户
        user.setCreateTime(new Date());
        user.setAvatar(SysUser.DEFAULT_AVATAR);
        user.setPassword(MD5Util.encrypt(user.getUsername(), SysUser.DEFAULT_PASSWORD));
        save(user);

        // 保存用户角色
        String[] roles = user.getRoleId().split(StringPool.COMMA);
        setUserRoles(user, roles);

        // 创建用户默认的个性化配置
        iSysUserConfigService.initDefaultUserConfig(String.valueOf(user.getUserId()));

        // 将用户相关信息保存到 Redis中
        userManager.loadUserRedisCache(user);
    }

    @Override
    @Transactional
    public void updateUser(SysUser user) throws Exception {
        // 更新用户
        user.setPassword(null);
        user.setUpdateTime(new Date());
        updateById(user);

        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, user.getUserId()));

        String[] roles = user.getRoleId().split(StringPool.COMMA);
        setUserRoles(user, roles);

        // 重新将用户信息，用户角色信息，用户权限信息 加载到 redis中
        cacheService.saveUser(user.getUsername());
        cacheService.saveRoles(user.getUsername());
        cacheService.savePermissions(user.getUsername());
    }

    @Override
    @Transactional
    public void deleteUsers(String[] userIds) throws Exception {
        // 先删除相应的缓存
        this.userManager.deleteUserRedisCache(userIds);

        List<String> list = Arrays.asList(userIds);

        removeByIds(list);

        // 删除用户角色
        this.iSysUserRoleService.deleteUserRolesByUserId(userIds);
        // 删除用户个性化配置
        this.iSysUserConfigService.deleteByUserId(userIds);
    }

    @Override
    @Transactional
    public void updateProfile(SysUser user) throws Exception {
        updateById(user);
        // 重新缓存用户信息
        cacheService.saveUser(user.getUsername());
    }

    @Override
    @Transactional
    public void updateAvatar(String username, String avatar) throws Exception {
        SysUser user = new SysUser();
        user.setAvatar(avatar);

        this.baseMapper.update(user, new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        // 重新缓存用户信息
        cacheService.saveUser(username);
    }

    @Override
    @Transactional
    public void updatePassword(String username, String password) throws Exception {
        SysUser user = new SysUser();
        user.setPassword(MD5Util.encrypt(username, password));

        this.baseMapper.update(user, new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        // 重新缓存用户信息
        cacheService.saveUser(username);
    }

    @Override
    @Transactional
    public void regist(String username, String password) throws Exception {
        SysUser user = new SysUser();
        user.setPassword(MD5Util.encrypt(username, password));
        user.setUsername(username);
        user.setCreateTime(new Date());
        user.setLockStatus(SysUser.STATUS_VALID);
        user.setSex(SysUser.SEX_UNKNOW);
        user.setAvatar(SysUser.DEFAULT_AVATAR);
        user.setDescription("注册用户");
        this.save(user);

        SysUserRole ur = new SysUserRole();
        ur.setUserId(user.getUserId());
        ur.setRoleId(2L); // 注册用户角色 ID
        this.sysUserRoleMapper.insert(ur);

        // 创建用户默认的个性化配置
        iSysUserConfigService.initDefaultUserConfig(String.valueOf(user.getUserId()));
        // 将用户相关信息保存到 Redis中
        userManager.loadUserRedisCache(user);

    }

    @Override
    @Transactional
    public void resetPassword(String[] usernames) throws Exception {
        for (String username : usernames) {

            SysUser user = new SysUser();
            user.setPassword(MD5Util.encrypt(username, SysUser.DEFAULT_PASSWORD));

            this.baseMapper.update(user, new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
            // 重新将用户信息加载到 redis中
            cacheService.saveUser(username);
        }

    }

    private void setUserRoles(SysUser user, String[] roles) {
        Arrays.stream(roles).forEach(roleId -> {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(user.getUserId());
            ur.setRoleId(Long.valueOf(roleId));
            this.sysUserRoleMapper.insert(ur);
        });
    }
}
