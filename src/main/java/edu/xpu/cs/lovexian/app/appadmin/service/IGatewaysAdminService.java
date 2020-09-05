package edu.xpu.cs.lovexian.app.appadmin.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGateways;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;

/**
 *  Service接口
 *
 * @author xpu
 * @date 2020-09-01 17:01:18
 */
@DS("slave")
public interface IGatewaysAdminService extends IService<AdminGateways> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param adminGateways adminGateways
     * @return IPage<AdminGateways>
     */
    IPage<AdminGateways> findGatewayss(QueryRequest request,AdminGateways adminGateways);
}