package edu.xpu.cs.lovexian.systemadmin.generator.controller;

import edu.xpu.cs.lovexian.common.annotation.ControllerEndpoint;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.exception.EarthSiteException;
import edu.xpu.cs.lovexian.systemadmin.generator.entity.GeneratorConfig;
import edu.xpu.cs.lovexian.systemadmin.generator.service.IGeneratorConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
@RestController
@RequestMapping("/generatorConfig")
public class GeneratorConfigController extends BaseController {

    @Autowired
    private IGeneratorConfigService iGeneratorConfigService;

    @GetMapping
    @RequiresPermissions("generator:configure:view")
    public EarthSiteResponse getGeneratorConfig() {
        return EarthSiteResponse.SUCCESS().data(iGeneratorConfigService.findGeneratorConfig());
    }

    @PostMapping("update")
    @RequiresPermissions("generator:configure:update")
    @ControllerEndpoint(operation = "修改GeneratorConfig", exceptionMessage = "修改GeneratorConfig失败")
    public EarthSiteResponse updateGeneratorConfig(@Valid GeneratorConfig generatorConfig) throws EarthSiteException {
        if (StringUtils.isBlank(generatorConfig.getId())){
            throw new EarthSiteException("配置id不能为空");
        }
        this.iGeneratorConfigService.updateGeneratorConfig(generatorConfig);
        return EarthSiteResponse.SUCCESS();
    }
}
