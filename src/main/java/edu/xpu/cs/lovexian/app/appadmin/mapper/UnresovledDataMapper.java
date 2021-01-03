package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminUnresovledData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * Mapper
 *
 * @author xpu
 * @date 2020-12-11 13:23:59
 */
@Component
@DS("slave")
public interface UnresovledDataMapper extends BaseMapper<AdminUnresovledData> {
    //把int改成long
    int getCount(@Param("settingId") String settingId);
    String getMessage(@Param("settingId") String settingId);
}
