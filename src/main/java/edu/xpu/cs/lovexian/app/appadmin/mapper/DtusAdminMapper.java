package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminDtus;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper
 *
 * @author xpu
 * @date 2020-09-01 21:29:32
 */
@Component
@DS("slave")
public interface DtusAdminMapper extends BaseMapper<AdminDtus> {

    IPage<AdminDtus> selectAll(Page page, @Param("adminDtus") AdminDtus adminDtus);

    IPage<AdminDtus> queryDtuInfo(Page page, @Param("adminDtus") AdminDtus adminDtus);

    IPage<AdminDtus> selectDtuId(Page page, @Param("dtuId") String dtuId);


    @Select("select elc_status,elec_charge,dtu_id from dtus")
    List<AdminDtus> selectThelastDtuInfo();


}
