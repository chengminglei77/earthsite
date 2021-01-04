package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGatewayDtu;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGateways;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper
 *
 * @author xpu
 * @date 2020-09-01 21:51:18
 */
@Component
@DS("slave")
public interface GatewayDtuAdminMapper extends BaseMapper<AdminGatewayDtu> {

    IPage<AdminGatewayDtu> selectAll(Page page, @Param("adminGatewayDtu") AdminGatewayDtu adminGatewayDtu);

    IPage<AdminGatewayDtu> queryGatewayDtuinfo(Page page, @Param("adminGatewayDtu") AdminGatewayDtu adminGatewayDtu);

    IPage<AdminGatewayDtu> selectGatewayDtu(Page page, @Param("gateId") String gateId);

    IPage<AdminGatewayDtu> selectDtusNotInGatewayDtu(Page page, @Param("dtuId") String dtuId);

    String selectGatewaySettingId(@Param("settingId")String settingId);
    //List<AdminGatewayDtu> selectGatewayDtu();


    @Select("select dtus.* from dtus where dtu_id in(select dtu_id from gateway_dtu where gateway_id= #{gateId,jdbcType=VARCHAR})")
    //IPage<AdminGatewayDtu> findDtusInGatewayDtu(QueryRequest queryRequest, @Param("gateId") String gateId);
    //IPage<AdminGatewayDtu> findDtusInGatewayDtu( @Param("gateId") String gateId);
    List<AdminGatewayDtu> findDtusInGatewayDtu(@Param("gateId") String gateId);
}
