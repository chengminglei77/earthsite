package edu.xpu.cs.lovexian.app.appadmin.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCommandInfo;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;

/**
 *  Service接口
 *
 * @author xpu
 * @date 2020-09-21 19:42:21
 */
@DS("slave")
public interface ICommandInfoAdminService extends IService<AdminCommandInfo> {

    /**
     * 查找不同类型的信息
     * @param request
     * @param adminCommandInfo
     * @return
     */

    IPage<AdminCommandInfo> findCommandInfos(QueryRequest request, AdminCommandInfo adminCommandInfo);

    IPage<AdminCommandInfo> queryCommandInfos(QueryRequest request, AdminCommandInfo adminCommandInfo);

    IPage<AdminCommandInfo> findCommandInfosByTypeId(QueryRequest request, AdminCommandInfo adminCommandInfo);

    AdminCommandInfo findCommand(AdminCommandInfo adminCommandInfo);
}
