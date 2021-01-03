package edu.xpu.cs.lovexian.app.appadmin.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminLawerInfo;

import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 律师信息表 Service接口
 *
 * @author xpu
 * @date 2019-12-25 15:25:48
 */
@DS("slave")
public interface ILawerInfoAdminService extends IService<AdminLawerInfo> {
    /**
     * 查询（分页）
     *
     * @param request        QueryRequest
     * @param adminLawerInfo adminLawerInfo
     * @return IPage<AdminLawerInfo>
     */
    IPage<AdminLawerInfo> findLawerInfos(QueryRequest request, AdminLawerInfo adminLawerInfo);


    boolean updateLawerInfo(AdminLawerInfo adminLawerInfo);

    /**
     * 查找不同类型的信息
     *
     * @param request
     * @param adminLawerInfo
     * @return
     */
    IPage<AdminLawerInfo> findLawerInfosByTypeId(QueryRequest request, AdminLawerInfo adminLawerInfo);

    /**
     * 爱公平里按照关键字段搜索信息
     *
     * @param request
     * @param adminLawerInfo
     * @return
     */
    IPage<AdminLawerInfo> queryLawerInfo(QueryRequest request, AdminLawerInfo adminLawerInfo);

    boolean deleteBatchLawer(String id);

    boolean deleteLawerInfo(String id);
}
