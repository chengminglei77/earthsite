package edu.xpu.cs.lovexian.app.appadmin.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDeviceStatistics;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;

/**
 * @author czy
 * @create 2020-10-25-19:45
 */
@DS("slave")
public interface IDeviceStatisticsAdminService extends IService<AdminDeviceStatistics> {
    //IPage<AdminDtus> findDtuss(QueryRequest request, AdminDtus adminDtus);
    IPage<AdminDeviceStatistics> findDeviceStatisticsByTypeId(QueryRequest request, AdminDeviceStatistics adminDeviceStatistics);

    boolean deleteDevice(String id);
}
