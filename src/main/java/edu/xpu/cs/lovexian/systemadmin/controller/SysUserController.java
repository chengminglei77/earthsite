package edu.xpu.cs.lovexian.systemadmin.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import edu.xpu.cs.lovexian.common.annotation.Log;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.common.exception.EarthSiteException;
import edu.xpu.cs.lovexian.common.utils.MD5Util;
import edu.xpu.cs.lovexian.systemadmin.domain.SysUser;
import edu.xpu.cs.lovexian.systemadmin.domain.SysUserConfig;
import edu.xpu.cs.lovexian.systemadmin.service.ISysUserConfigService;
import edu.xpu.cs.lovexian.systemadmin.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
@Validated
@RestController
@RequestMapping("/system/admin")
public class SysUserController extends BaseController {
    private String message;

    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ISysUserConfigService iSysUserConfigService;

    @GetMapping("check/{username}")
    public EarthSiteResponse checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
        boolean exist = this.iSysUserService.findByName(username) == null;
        return EarthSiteResponse.SUCCESS().data(exist);
    }

    @GetMapping("/{username}")
    public EarthSiteResponse detail(@NotBlank(message = "{required}") @PathVariable String username) {
        SysUser user = this.iSysUserService.findDetailByName(username);
        return EarthSiteResponse.SUCCESS().data(user);
    }

    @GetMapping("/list")
    @RequiresPermissions("user:view")
    public EarthSiteResponse userList(QueryRequest queryRequest, SysUser user) {
        return EarthSiteResponse.SUCCESS().data(getDataTable(iSysUserService.findUserDetail(user, queryRequest)));
    }

    @Log("新增用户")
    @PostMapping
    @RequiresPermissions("user:add")
    public EarthSiteResponse addUser(@Valid SysUser user) throws EarthSiteException {
        try {
            this.iSysUserService.createUser(user);
        } catch (Exception e) {
            message = "新增用户失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS().message("添加成功");
    }

    @Log("修改用户")
    @PutMapping
    @RequiresPermissions("user:update")
    public EarthSiteResponse updateUser(@Valid SysUser user) throws EarthSiteException {
        try {
            this.iSysUserService.updateUser(user);
        } catch (Exception e) {
            message = "修改用户失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @Log("删除用户")
    @DeleteMapping("/{userIds}")
    @RequiresPermissions("user:delete")
    public EarthSiteResponse deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) throws EarthSiteException {
        try {
            String[] ids = userIds.split(StringPool.COMMA);
            this.iSysUserService.deleteUsers(ids);
        } catch (Exception e) {
            message = "删除用户失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @PutMapping("/profile")
    public EarthSiteResponse updateProfile(SysUser user) throws EarthSiteException {
        try {
            this.iSysUserService.updateProfile(user);
        } catch (Exception e) {
            message = "修改个人信息失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @PutMapping("/avatar")
    public EarthSiteResponse updateAvatar(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String avatar) throws EarthSiteException {
        try {
            this.iSysUserService.updateAvatar(username, avatar);
        } catch (Exception e) {
            message = "修改头像失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @PutMapping("/userconfig")
    public void updateUserConfig(@Valid SysUserConfig userConfig) throws EarthSiteException {
        try {
            this.iSysUserConfigService.update(userConfig);
        } catch (Exception e) {
            message = "修改个性化配置失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
    }

    @GetMapping("/password/check")
    public EarthSiteResponse checkPassword(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) {
        String encryptPassword = MD5Util.encrypt(username, password);
        SysUser user = iSysUserService.findByName(username);
        if (user != null){
            return EarthSiteResponse.SUCCESS().data(StringUtils.equals(user.getPassword(), encryptPassword));
        } else{
            return EarthSiteResponse.SUCCESS().data(false);
        }
    }

    @PutMapping("/password")
    public EarthSiteResponse updatePassword(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) {
        try {
            iSysUserService.updatePassword(username, password);
        } catch (Exception e) {
            message = "修改密码失败";
            log.error(message, e);
//            throw new LoveXianException(message);
            return EarthSiteResponse.SUCCESS().message(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @PutMapping("/password/reset")
    @RequiresPermissions("user:reset")
    public EarthSiteResponse resetPassword(@NotBlank(message = "{required}") String usernames) throws EarthSiteException {
        try {
            String[] usernameArr = usernames.split(StringPool.COMMA);
            this.iSysUserService.resetPassword(usernameArr);
        } catch (Exception e) {
            message = "重置用户密码失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @GetMapping("/excel")
    @RequiresPermissions("user:export")
    public EarthSiteResponse export(QueryRequest queryRequest, SysUser user, HttpServletResponse response) throws EarthSiteException {
        try {
            List<SysUser> users = this.iSysUserService.findUserDetail(user, queryRequest).getRecords();
            ExcelKit.$Export(SysUser.class, response).downXlsx(users, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }
}
