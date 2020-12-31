package edu.xpu.cs.lovexian.app.appadmin.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtus;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
/**
 *  Service接口
 *
 * @author xpu
 * @date 2020-09-01 21:29:32
 */
@DS("slave")
public interface IDtusAdminService extends IService<AdminDtus> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param adminDtus adminDtus
     * @return IPage<AdminDtus>
     */
    IPage<AdminDtus> findDtuss(QueryRequest request, AdminDtus adminDtus);

    IPage<AdminDtus> findDtusByTypeId(QueryRequest request, AdminDtus adminDtus);


    /**
     * 批量删除和通过id查询删除同时使用一个service
     *
     * @param id
     * @return
     */
    boolean deleteDtus(String id);
    public boolean restoreDtus(String id);

    boolean completelyDeleteDtus(String id);

    boolean getDtuId(String dtuId);

    IPage<AdminDtus> queryDtuInfo(QueryRequest request, AdminDtus adminDtus);

   // public boolean autoUpdateDtus(AdminDtus adminDtus);

}
