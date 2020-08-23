package edu.xpu.cs.lovexian.common.aspect;

import edu.xpu.cs.lovexian.common.authentication.JWTUtil;
import edu.xpu.cs.lovexian.common.properties.EarthSiteProperties;
import edu.xpu.cs.lovexian.common.utils.HttpContextUtil;
import edu.xpu.cs.lovexian.common.utils.IPUtil;
import edu.xpu.cs.lovexian.systemadmin.domain.SysLog;
import edu.xpu.cs.lovexian.systemadmin.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**                                                                                ____________________
      _                _                                                           < 神兽护体，永无bug! >
    | |__  _   _  ___| |__   ___ _ __   __ _ _ __   ___ _ __   __ _                --------------------
   | '_ \| | | |/ __| '_ \ / _ \ '_ \ / _` | '_ \ / _ \ '_ \ / _` |                       \   ^__^
  | | | | |_| | (__| | | |  __/ | | | (_| | |_) |  __/ | | | (_| |                        \  (oo)\_______
 |_| |_|\__,_|\___|_| |_|\___|_| |_|\__, | .__/ \___|_| |_|\__, |                           (__)\       )\/\
                                   |___/|_|                |___/                                ||----w |
                                                                                                ||     ||
 * @author hcp
 * @version 1.0.0
 * @create 20:57 2019/11/1
 * AOP 记录用户操作日志
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private EarthSiteProperties loveXianProperties;

    @Autowired
    private ISysLogService iSysLogService;

    @Pointcut("@annotation(edu.xpu.cs.lovexian.common.annotation.Log)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        // 执行方法
        result = point.proceed();
        // 获取 request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 设置 IP 地址
        String ip = IPUtil.getIpAddr(request);
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (loveXianProperties.isOpenAopLog()) {
            // 保存日志
            String token = (String) SecurityUtils.getSubject().getPrincipal();
            String username = "";
            if (StringUtils.isNotBlank(token)) {
                username = JWTUtil.getUsername(token);
            }

            SysLog log = new SysLog();
            log.setUsername(username);
            log.setIp(ip);
            log.setOperTime(time);
            iSysLogService.saveLog(point, log);
        }
        return result;
    }
}
