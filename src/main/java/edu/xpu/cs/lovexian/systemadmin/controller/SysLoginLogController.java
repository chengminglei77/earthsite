package edu.xpu.cs.lovexian.systemadmin.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.xpu.cs.lovexian.common.annotation.Limit;
import edu.xpu.cs.lovexian.common.authentication.JWTToken;
import edu.xpu.cs.lovexian.common.authentication.JWTUtil;
import edu.xpu.cs.lovexian.common.domain.ActiveUser;
import edu.xpu.cs.lovexian.common.domain.EarthSiteConstant;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.properties.EarthSiteProperties;
import edu.xpu.cs.lovexian.common.response.RespStatus;
import edu.xpu.cs.lovexian.common.service.RedisService;
import edu.xpu.cs.lovexian.common.utils.*;
import edu.xpu.cs.lovexian.systemadmin.domain.SysLoginLog;
import edu.xpu.cs.lovexian.systemadmin.domain.SysUser;
import edu.xpu.cs.lovexian.systemadmin.domain.SysUserConfig;
import edu.xpu.cs.lovexian.systemadmin.manager.UserManager;
import edu.xpu.cs.lovexian.systemadmin.mapper.SysLoginLogMapper;
import edu.xpu.cs.lovexian.systemadmin.service.ISysLoginLogService;
import edu.xpu.cs.lovexian.systemadmin.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;

import static edu.xpu.cs.lovexian.common.utils.LoveXianUtil.getCurrentUser;

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
@Api(value="管理员相关接口",description = "管理员相关接口，提供数据模型的管理、查询、登录接口")
@Validated
@RestController
@RequestMapping("/system/login")
public class SysLoginLogController {
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ISysLoginLogService iSysLoginLogService;
    @Autowired
    private SysLoginLogMapper sysLoginLogMapper;
    @Autowired
    private EarthSiteProperties properties;
    @Autowired
    private ObjectMapper mapper;

    @ApiOperation(value="使用用户名和密码登录", notes="使用username和password来登录")
    @ApiImplicitParams(
        {
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
        }
    )
    @PostMapping("/login")
    @Limit(key = "login", period = 60, count = 20, name = "登录接口", prefix = "limit")
    public EarthSiteResponse login(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password, HttpServletRequest request) throws Exception {
        username = StringUtils.lowerCase(username);
        password = MD5Util.encrypt(username, password);

        final String errorMessage = "用户名或密码错误";
        SysUser user = this.userManager.getUser(username);
        if (user == null){
            return EarthSiteResponse.SUCCESS().message("用户名或密码错误").status(RespStatus.fail.getCode());
        }
        if (!StringUtils.equals(user.getPassword(), password)){
            return EarthSiteResponse.SUCCESS().message("用户名或密码错误").status(RespStatus.fail.getCode());
        }
        if (SysUser.STATUS_LOCK.equals(user.getLockStatus())){
            return EarthSiteResponse.SUCCESS().message("账号已被锁定,请联系管理员！").status(RespStatus.fail.getCode());
        }
        // 更新用户登录时间
        this.iSysUserService.updateLoginTime(username);
        // 保存登录记录
        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setUsername(username);
        this.iSysLoginLogService.saveLoginLog(loginLog);

        String token = LoveXianUtil.encryptToken(JWTUtil.sign(username, password));
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(properties.getShiro().getJwtTimeOut());
        String expireTimeStr = DateUtil.formatFullTime(expireTime);
        JWTToken jwtToken = new JWTToken(token, expireTimeStr);

        String userId = this.saveTokenToRedis(user, jwtToken, request);
        user.setId(userId);

        Map<String, Object> userInfo = this.generateUserInfo(jwtToken, user);
        return EarthSiteResponse.SUCCESS().message("认证成功").data(userInfo);
    }

    @GetMapping("/index/{username}")
    public EarthSiteResponse index(@NotBlank(message = "{required}") @PathVariable String username) {
        Map<String, Object> data = new HashMap<>();
        // 获取系统访问记录
        Long totalVisitCount = sysLoginLogMapper.findTotalVisitCount();
        data.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = sysLoginLogMapper.findTodayVisitCount();
        data.put("todayVisitCount", todayVisitCount);
        Long todayIp = sysLoginLogMapper.findTodayIp();
        data.put("todayIp", todayIp);
        // 获取近期系统访问记录
        List<Map<String, Object>> lastSevenVisitCount = sysLoginLogMapper.findLastSevenDaysVisitCount(null);
        data.put("lastSevenVisitCount", lastSevenVisitCount);
        SysUser param = new SysUser();
        param.setUsername(username);
        List<Map<String, Object>> lastSevenUserVisitCount = sysLoginLogMapper.findLastSevenDaysVisitCount(param);
        data.put("lastSevenUserVisitCount", lastSevenUserVisitCount);
        return EarthSiteResponse.SUCCESS().data(data);
    }

    @RequiresPermissions("user:online")
    @GetMapping("/online")
    public EarthSiteResponse userOnline(String username) throws Exception {
        String now = DateUtil.formatFullTime(LocalDateTime.now());
        Set<String> userOnlineStringSet = redisService.zrangeByScore(EarthSiteConstant.ACTIVE_USERS_ZSET_PREFIX, now, "+inf");
        List<ActiveUser> activeUsers = new ArrayList<>();
        for (String userOnlineString : userOnlineStringSet) {
            ActiveUser activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
            if(activeUser.getUsername().equals(getCurrentUser().getUsername())){
                activeUser.setCurrent(true);
            }
            activeUser.setToken(null);
            if (StringUtils.isNotBlank(username)) {
                if (StringUtils.equalsIgnoreCase(username, activeUser.getUsername())){
                    activeUsers.add(activeUser);
                }
            } else {
                activeUsers.add(activeUser);
            }
        }
        Map<String, Object> rspData = new HashMap<>();
        rspData.put("rows",activeUsers);
        rspData.put("total",activeUsers.size());
        return EarthSiteResponse.SUCCESS().data(rspData);
    }

    @DeleteMapping("/kickout/{id}")
    @RequiresPermissions("user:kickout")
    public EarthSiteResponse kickout(@NotBlank(message = "{required}") @PathVariable String id) throws Exception {
        String now = DateUtil.formatFullTime(LocalDateTime.now());
        Set<String> userOnlineStringSet = redisService.zrangeByScore(EarthSiteConstant.ACTIVE_USERS_ZSET_PREFIX, now, "+inf");
        ActiveUser kickoutUser = null;
        String kickoutUserString = "";
        for (String userOnlineString : userOnlineStringSet) {
            ActiveUser activeUser = mapper.readValue(userOnlineString, ActiveUser.class);
            if (StringUtils.equals(activeUser.getId(), id)) {
                kickoutUser = activeUser;
                kickoutUserString = userOnlineString;
            }
        }
        if (kickoutUser != null && StringUtils.isNotBlank(kickoutUserString)) {
            // 删除 zset中的记录
            redisService.zrem(EarthSiteConstant.ACTIVE_USERS_ZSET_PREFIX, kickoutUserString);
            // 删除对应的 token缓存
            redisService.del(EarthSiteConstant.TOKEN_CACHE_PREFIX + kickoutUser.getToken() + "." + kickoutUser.getIp());
        }
        return EarthSiteResponse.SUCCESS();
    }

    @GetMapping("/logout/{id}")
    public EarthSiteResponse logout(@NotBlank(message = "{required}") @PathVariable String id) throws Exception {
        return this.kickout(id);
    }

    @PostMapping("/regist")
    public void regist(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) throws Exception {
        this.iSysUserService.regist(username, password);
    }

    private String saveTokenToRedis(SysUser user, JWTToken token, HttpServletRequest request) throws Exception {
        String ip = IPUtil.getIpAddr(request);

        // 构建在线用户
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUsername(user.getUsername());
        activeUser.setIp(ip);
        activeUser.setToken(token.getToken());
        activeUser.setLoginAddress(AddressUtil.getCityInfo(ip));

        // zset 存储登录用户，score 为过期时间戳
        this.redisService.zadd(EarthSiteConstant.ACTIVE_USERS_ZSET_PREFIX, Double.valueOf(token.getExipreAt()), mapper.writeValueAsString(activeUser));
        // redis 中存储这个加密 token，key = 前缀 + 加密 token + .ip
        this.redisService.set(EarthSiteConstant.TOKEN_CACHE_PREFIX + token.getToken() + StringPool.DOT + ip, token.getToken(), properties.getShiro().getJwtTimeOut() * 1000);

        return activeUser.getId();
    }

    /**
     * 生成前端需要的用户信息，包括：
     * 1. token
     * 2. Vue Router
     * 3. 用户角色
     * 4. 用户权限
     * 5. 前端系统个性化配置信息
     *
     * @param token token
     * @param user  用户信息
     * @return UserInfo
     */
    private Map<String, Object> generateUserInfo(JWTToken token, SysUser user) {
        String username = user.getUsername();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", token.getToken());
        userInfo.put("exipreTime", token.getExipreAt());

        Set<String> roles = this.userManager.getUserRoles(username);
        userInfo.put("roles", roles);

        Set<String> permissions = this.userManager.getUserPermissions(username);
        userInfo.put("permissions", permissions);

        SysUserConfig userConfig = this.userManager.getUserConfig(String.valueOf(user.getUserId()));
        userInfo.put("config", userConfig);

        user.setPassword("it's a secret");
        userInfo.put("user", user);
        return userInfo;
    }
}
