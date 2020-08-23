package edu.xpu.cs.lovexian.systemadmin.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import edu.xpu.cs.lovexian.common.annotation.Log;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.common.exception.EarthSiteException;
import edu.xpu.cs.lovexian.systemadmin.domain.SysLog;
import edu.xpu.cs.lovexian.systemadmin.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/system/log")
public class SysLogController extends BaseController {
    private String message;

    @Autowired
    private ISysLogService iSysLogService;

    @GetMapping("/list")
    @RequiresPermissions("log:view")
    public EarthSiteResponse logList(QueryRequest request, SysLog sysLog) {
        return EarthSiteResponse.SUCCESS().data(getDataTable(iSysLogService.findLogs(request, sysLog)));
    }

    @Log("删除系统日志")
    @DeleteMapping("/delete/{ids}")
    @RequiresPermissions("log:delete")
    public EarthSiteResponse deleteLogss(@NotBlank(message = "{required}") @PathVariable String ids) throws EarthSiteException {
        try {
            String[] logIds = ids.split(StringPool.COMMA);
            this.iSysLogService.deleteLogs(logIds);
        } catch (Exception e) {
            message = "删除日志失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }

    @GetMapping("excel")
    @RequiresPermissions("log:export")
    public EarthSiteResponse export(QueryRequest request, SysLog sysLog, HttpServletResponse response) throws EarthSiteException {
        try {
            List<SysLog> sysLogs = this.iSysLogService.findLogs(request, sysLog).getRecords();
            ExcelKit.$Export(SysLog.class, response).downXlsx(sysLogs, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new EarthSiteException(message);
        }
        return EarthSiteResponse.SUCCESS();
    }
}
