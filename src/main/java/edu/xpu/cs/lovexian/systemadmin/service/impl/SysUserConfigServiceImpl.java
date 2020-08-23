package edu.xpu.cs.lovexian.systemadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.xpu.cs.lovexian.common.service.CacheService;
import edu.xpu.cs.lovexian.systemadmin.domain.SysUserConfig;
import edu.xpu.cs.lovexian.systemadmin.mapper.SysUserConfigMapper;
import edu.xpu.cs.lovexian.systemadmin.service.ISysUserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
@Service("iSysUserConfigService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysUserConfigServiceImpl extends ServiceImpl<SysUserConfigMapper, SysUserConfig> implements ISysUserConfigService {
    @Autowired
    private CacheService cacheService;

    @Override
    public SysUserConfig findByUserId(String userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    @Transactional
    public void initDefaultUserConfig(String userId) {
        SysUserConfig userConfig = new SysUserConfig();
        userConfig.setUserId(Long.valueOf(userId));
        userConfig.setColor(SysUserConfig.DEFAULT_COLOR);
        userConfig.setFixHeader(SysUserConfig.DEFAULT_FIX_HEADER);
        userConfig.setFixSiderbar(SysUserConfig.DEFAULT_FIX_SIDERBAR);
        userConfig.setLayout(SysUserConfig.DEFAULT_LAYOUT);
        userConfig.setTheme(SysUserConfig.DEFAULT_THEME);
        userConfig.setMultiPage(SysUserConfig.DEFAULT_MULTIPAGE);
        baseMapper.insert(userConfig);
    }

    @Override
    @Transactional
    public void deleteByUserId(String... userIds) {
        List<String> list = Arrays.asList(userIds);
        baseMapper.deleteBatchIds(list);
    }

    @Override
    @Transactional
    public void update(SysUserConfig userConfig) throws Exception {
        baseMapper.updateById(userConfig);
        cacheService.saveUserConfigs(String.valueOf(userConfig.getUserId()));
    }
}
