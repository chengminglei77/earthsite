package edu.xpu.cs.lovexian.app.appadmin.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCommandInfo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Component;

/**
 *  Mapper
 *
 * @author xpu
 * @date 2020-09-21 19:42:21
 */
@Component
@DS("slave")
public interface CommandInfoAdminMapper extends BaseMapper<AdminCommandInfo> {
    IPage<AdminCommandInfo> selectAll(Page page, @Param("adminCommandInfo") AdminCommandInfo adminCommandInfo);

    IPage<AdminCommandInfo> queryCommandInfo(Page page, @Param("adminCommandInfo") AdminCommandInfo adminCommandInfo);
}
