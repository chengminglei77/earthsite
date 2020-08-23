package edu.xpu.cs.lovexian.systemadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.xpu.cs.lovexian.systemadmin.domain.SysRole;
import org.apache.ibatis.annotations.Param;

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
public interface SysRoleMapper extends BaseMapper<SysRole> {
    List<SysRole> findUserRole(String userName);


    /**
     * 查找角色详情,包括所拥有权限
     *
     * @param page 分页
     * @param role 角色
     * @return IPage<User>
     */
    IPage<SysRole> findRolAndMenuPage(Page page, @Param("role") SysRole role);
}
