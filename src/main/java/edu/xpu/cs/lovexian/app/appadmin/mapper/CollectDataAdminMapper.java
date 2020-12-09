package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCollectData;
import org.springframework.stereotype.Component;


/**
 * author:zhanganjie
 * date:2020-12-7
 */
@Component
@DS("slave")
public interface CollectDataAdminMapper extends BaseMapper<AdminCollectData> {

}
