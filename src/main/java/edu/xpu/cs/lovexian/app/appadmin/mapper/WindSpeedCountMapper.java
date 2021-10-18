package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.xpu.cs.lovexian.app.appadmin.entity.WindSpeedCount;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: zaj
 * @Date: 2021/10/12/21:44
 * @Description:
 */
@Component
@DS("slave")
public interface WindSpeedCountMapper extends BaseMapper<WindSpeedCount> {
}
