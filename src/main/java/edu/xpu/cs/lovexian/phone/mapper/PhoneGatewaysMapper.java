package edu.xpu.cs.lovexian.phone.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.xpu.cs.lovexian.phone.entity.Gateways;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * (Gateways)表数据库访问层
 *
 * @author makejava
 * @since 2021-05-06 11:37:31
 */
@Component
@DS("slave")
public interface PhoneGatewaysMapper extends BaseMapper<Gateways> {

}