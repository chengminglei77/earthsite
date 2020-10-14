package edu.xpu.cs.lovexian.app.appadmin.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGatewayDtu;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;

/**
 *  Service接口
 *
 * @author xpu
 * @date 2020-09-01 21:51:18
 */
@DS("slave")
public interface IGatewayDtuAdminService extends IService<AdminGatewayDtu> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param adminGatewayDtu adminGatewayDtu
     * @return IPage<AdminGatewayDtu>
     */
    IPage<AdminGatewayDtu> findGatewayDtus(QueryRequest request, AdminGatewayDtu adminGatewayDtu);

    IPage<AdminGatewayDtu> findGatewayDtuByTypeId(QueryRequest request, AdminGatewayDtu adminGatewayDtu);

    boolean updateGatewayDtu(AdminGatewayDtu adminGatewayDtu);

    IPage<AdminGatewayDtu> getGatewayDtu(QueryRequest request,AdminGatewayDtu adminGatewayDtu);


}
