package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDecodeData;
import org.springframework.stereotype.Component;


/**
 * Mapper
 *
 * @author xpu
 * @date 2020-12-13 13:50:59
 */
@Component
@DS("slave")
public interface DecodeDataMapper extends BaseMapper<AdminDecodeData> {

}
