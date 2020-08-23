package ${basePackage}.${servicePackage};

import ${basePackage}.${entityPackage}.${className};

import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * ${tableComment} Service接口
 *
 * @author ${author}
 * @date ${date}
 */
@DS("slave")
public interface I${className}AdminService extends IService<Admin${className}> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param admin${className} admin${className}
     * @return IPage<Admin${className}>
     */
    IPage<Admin${className}> find${className}s(QueryRequest request, Admin${className} admin${className});


}
