package edu.xpu.cs.lovexian.common.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.xpu.cs.lovexian.common.domain.EarthSiteConstant;
import edu.xpu.cs.lovexian.common.exception.RedisConnectException;
import edu.xpu.cs.lovexian.common.service.CacheService;
import edu.xpu.cs.lovexian.common.service.RedisService;
import edu.xpu.cs.lovexian.systemadmin.domain.SysMenu;
import edu.xpu.cs.lovexian.systemadmin.domain.SysRole;
import edu.xpu.cs.lovexian.systemadmin.domain.SysUser;
import edu.xpu.cs.lovexian.systemadmin.domain.SysUserConfig;
import edu.xpu.cs.lovexian.systemadmin.mapper.SysUserMapper;
import edu.xpu.cs.lovexian.systemadmin.service.ISysMenuService;
import edu.xpu.cs.lovexian.systemadmin.service.ISysRoleService;
import edu.xpu.cs.lovexian.systemadmin.service.ISysUserConfigService;
import edu.xpu.cs.lovexian.systemadmin.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
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
@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private ISysMenuService iSysMenuService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysUserConfigService iSysUserConfigService;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void testConnect() throws Exception {
        this.redisService.exists("test");
    }

    @Override
    public SysUser getUser(String username) throws Exception {
        String userString = this.redisService.get(EarthSiteConstant.USER_CACHE_PREFIX + username);
        if (StringUtils.isBlank(userString)){
            throw new Exception();
        }else {
            return this.mapper.readValue(userString, SysUser.class);
        }
    }

    @Override
    public List<SysRole> getRoles(String username) throws Exception {
        String roleListString = this.redisService.get(EarthSiteConstant.USER_ROLE_CACHE_PREFIX + username);
        if (StringUtils.isBlank(roleListString)) {
            throw new Exception();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, SysRole.class);
            return this.mapper.readValue(roleListString, type);
        }
    }

    @Override
    public List<SysMenu> getPermissions(String username) throws Exception {
        String permissionListString = this.redisService.get(EarthSiteConstant.USER_PERMISSION_CACHE_PREFIX + username);
        if (StringUtils.isBlank(permissionListString)) {
            throw new Exception();
        } else {
            JavaType type = mapper.getTypeFactory().constructParametricType(List.class, SysMenu.class);
            return this.mapper.readValue(permissionListString, type);
        }
    }

    @Override
    public SysUserConfig getUserConfig(String userId) throws Exception {
        String userConfigString = this.redisService.get(EarthSiteConstant.USER_CONFIG_CACHE_PREFIX + userId);
        if (StringUtils.isBlank(userConfigString)) {
            throw new Exception();
        }else {
            return this.mapper.readValue(userConfigString, SysUserConfig.class);
        }
    }

    @Override
    public void saveUser(SysUser user) throws Exception {
        String username = user.getUsername();
        this.deleteUser(username);
        redisService.set(EarthSiteConstant.USER_CACHE_PREFIX + username, mapper.writeValueAsString(user));
    }

    @Override
    public void saveUser(String username) throws Exception {
        SysUser user = userMapper.findDetail(username);
        this.deleteUser(username);
        redisService.set(EarthSiteConstant.USER_CACHE_PREFIX + username, mapper.writeValueAsString(user));
    }

    @Override
    public void saveRoles(String username) throws Exception {
        List<SysRole> roleList = this.iSysRoleService.findUserRole(username);
        if (!roleList.isEmpty()) {
            this.deleteRoles(username);
            redisService.set(EarthSiteConstant.USER_ROLE_CACHE_PREFIX + username, mapper.writeValueAsString(roleList));
        }

    }

    @Override
    public void savePermissions(String username) throws Exception {
        List<SysMenu> permissionList = this.iSysMenuService.findUserPermissions(username);
        if (!permissionList.isEmpty()) {
            this.deletePermissions(username);
            redisService.set(EarthSiteConstant.USER_PERMISSION_CACHE_PREFIX + username, mapper.writeValueAsString(permissionList));
        }
    }

    @Override
    public void saveUserConfigs(String userId) throws Exception {
        SysUserConfig userConfig = this.iSysUserConfigService.findByUserId(userId);
        if (userConfig != null) {
            this.deleteUserConfigs(userId);
            redisService.set(EarthSiteConstant.USER_CONFIG_CACHE_PREFIX + userId, mapper.writeValueAsString(userConfig));
        }
    }

    @Override
    public void deleteUser(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(EarthSiteConstant.USER_CACHE_PREFIX + username);
    }

    @Override
    public void deleteRoles(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(EarthSiteConstant.USER_ROLE_CACHE_PREFIX + username);
    }

    @Override
    public void deletePermissions(String username) throws Exception {
        username = username.toLowerCase();
        redisService.del(EarthSiteConstant.USER_PERMISSION_CACHE_PREFIX + username);
    }

    @Override
    public void deleteUserConfigs(String userId) throws Exception {
        redisService.del(EarthSiteConstant.USER_CONFIG_CACHE_PREFIX + userId);
    }


    @Override
    public void setTusUrl(String fingerprint, URL url){
        try {
            redisService.set(fingerprint,mapper.writeValueAsString(url));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public URL getTusUrl(String fingerprint) {
        try{
            if(redisService.exists(fingerprint)){
                String urlStr = redisService.get(fingerprint);
                return mapper.readValue(urlStr, URL.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removeTusUrl(String fingerprint){
        try {
            redisService.del(fingerprint);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public long activeUserInc()throws RedisConnectException {
        return redisService.activeUserInc(EarthSiteConstant.ACTIVE_CELL_USERS_NUMBER);
    }

    @Override
    public long activeUserDec()throws RedisConnectException{
        return redisService.activeUserDec(EarthSiteConstant.ACTIVE_CELL_USERS_NUMBER);
    }

    @Override
    public long getActiveUser() throws Exception {
        return Long.valueOf(redisService.get(EarthSiteConstant.ACTIVE_CELL_USERS_NUMBER));
    }

}
