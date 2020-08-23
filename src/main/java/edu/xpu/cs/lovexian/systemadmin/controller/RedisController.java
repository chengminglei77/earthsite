package edu.xpu.cs.lovexian.systemadmin.controller;

import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.RedisInfo;
import edu.xpu.cs.lovexian.common.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/system/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @GetMapping("info")
    public EarthSiteResponse getRedisInfo() throws Exception {
        List<RedisInfo> infoList = this.redisService.getRedisInfo();
        return EarthSiteResponse.SUCCESS().data(infoList);
    }

    @GetMapping("keysSize")
    public EarthSiteResponse getKeysSize() throws Exception {
        return EarthSiteResponse.SUCCESS().data(redisService.getKeysSize());
    }

    @GetMapping("memoryInfo")
    public EarthSiteResponse getMemoryInfo() throws Exception {
        return EarthSiteResponse.SUCCESS().data(redisService.getMemoryInfo());
    }
}
