package ${basePackage}.${serviceImplPackage};

import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import ${basePackage}.${entityPackage}.${className};
import ${basePackage}.${mapperPackage}.${className}Mapper;
import ${basePackage}.${servicePackage}.I${className}Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * ${tableComment} Service实现
 *
 * @author ${author}
 * @date ${date}
 */
@Service("${className?uncap_first}AdminService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ${className}AdminServiceImpl extends ServiceImpl<${className}AdminMapper, Admin${className}> implements I${className}AdminService {

    @Autowired
    private ${className}AdminMapper ${className?uncap_first}AdminMapper;

    @Override
    public IPage<${className}> find${className}s(QueryRequest request, Admin${className} admin${className}) {
        QueryWrapper<Admin${className}> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        Page<Admin${className}> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

}
