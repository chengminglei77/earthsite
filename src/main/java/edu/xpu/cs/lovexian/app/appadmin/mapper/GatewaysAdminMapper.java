package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGateways;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper
 *
 * @author xpu
 * @date 2020-09-01 17:01:18
 */
@Component
@DS("slave")
public interface GatewaysAdminMapper extends BaseMapper<AdminGateways> {
    IPage<AdminGateways> selectAll(Page page, @Param("adminGateways") AdminGateways adminGateways);

    IPage<AdminGateways> queryGatewaysInfo(Page page, @Param("adminGateways") AdminGateways adminGateways);

    @Select("select elc_status,elec_charge,created_at,gate_id from gateways")
    List<AdminGateways> selectThelastGatewayInfo();




}
