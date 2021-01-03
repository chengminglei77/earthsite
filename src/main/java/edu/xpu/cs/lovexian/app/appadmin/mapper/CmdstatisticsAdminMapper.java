package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminUnresovledData;
import org.springframework.stereotype.Component;

/**
 * Mapper
 *
 * @author xpu
 * @date 2020-09-01 17:01:18
 */
@Component
@DS("slave")
public interface CmdstatisticsAdminMapper extends BaseMapper<AdminUnresovledData> {

}

