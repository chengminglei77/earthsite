package edu.xpu.cs.lovexian.systemadmin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.systemadmin.domain.SysRole;

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
public interface ISysRoleService extends IService<SysRole> {
    IPage<SysRole> findRoles(SysRole role, QueryRequest request);

    List<SysRole> findUserRole(String userName);

    IPage<SysRole> findRolesAndMenus(SysRole role, QueryRequest request);

    SysRole findByName(String roleName);

    void createRole(SysRole role);

    void deleteRoles(String[] roleIds) throws Exception;

    void updateRole(SysRole role) throws Exception;
}
