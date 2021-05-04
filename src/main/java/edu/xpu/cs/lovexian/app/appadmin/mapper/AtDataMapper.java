package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminAtData;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminAtData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * (AtData)表数据库访问层
 *
 * @author makejava
 * @since 2021-05-03 17:23:44
 */
@Component
@DS("slave")
public interface AtDataMapper extends BaseMapper<AdminAtData> {

    AdminAtData selectLatestData(String at,String date);


}