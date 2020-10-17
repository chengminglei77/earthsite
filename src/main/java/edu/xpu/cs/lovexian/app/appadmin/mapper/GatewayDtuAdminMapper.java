package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminGatewayDtu;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Component;

/**
 *  Mapper
 *
 * @author xpu
 * @date 2020-09-01 21:51:18
 */
@Component
@DS("slave")
public interface GatewayDtuAdminMapper extends BaseMapper<AdminGatewayDtu> {

    IPage<AdminGatewayDtu> selectAll(Page page, @Param("adminGatewayDtu") AdminGatewayDtu adminGatewayDtu);

    IPage<AdminGatewayDtu> queryGatewayDtuinfo(Page page, @Param("adminGatewayDtu") AdminGatewayDtu adminGatewayDtu);

    IPage<AdminGatewayDtu> selectGatewayDtu(Page page,@Param("gateId") String gateId);

    //List<AdminGatewayDtu> selectGatewayDtu();
}
