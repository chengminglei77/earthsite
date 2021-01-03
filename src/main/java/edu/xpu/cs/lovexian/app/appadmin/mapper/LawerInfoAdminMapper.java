package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminLawerInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Component;

/**
 * 律师信息表 Mapper
 *
 * @author xpu
 * @date 2019-12-25 15:25:48
 */
@Component
@DS("slave")
public interface LawerInfoAdminMapper extends BaseMapper<AdminLawerInfo> {

    /**
     * 查询所有活动信息（分页）
     *
     * @param page
     * @param adminLawerInfo
     * @return
     */
    IPage<AdminLawerInfo> selectAll(Page page, @Param("adminLawerInfo") AdminLawerInfo adminLawerInfo);

    IPage<AdminLawerInfo> queryLawerInfo(Page page, @Param("adminLawerInfo") AdminLawerInfo adminLawerInfo);
}
