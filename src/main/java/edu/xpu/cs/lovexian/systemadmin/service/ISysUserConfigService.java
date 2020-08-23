package edu.xpu.cs.lovexian.systemadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.xpu.cs.lovexian.systemadmin.domain.SysUserConfig;

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
public interface ISysUserConfigService extends IService<SysUserConfig> {
    /**
     * 通过用户 ID 获取前端系统个性化配置
     *
     * @param userId 用户 ID
     * @return 前端系统个性化配置
     */
    SysUserConfig findByUserId(String userId);

    /**
     * 生成用户默认个性化配置
     *
     * @param userId 用户 ID
     */
    void initDefaultUserConfig(String userId);

    /**
     * 通过用户 ID 删除个性化配置
     *
     * @param userIds 用户 ID 数组
     */
    void deleteByUserId(String... userIds);

    /**
     * 更新用户个性化配置
     *
     * @param  userConfig 用户个性化配置
     */
    void update(SysUserConfig userConfig) throws Exception;
}
