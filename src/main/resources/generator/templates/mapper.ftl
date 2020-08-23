package ${basePackage}.${mapperPackage};

import ${basePackage}.${entityPackage}.${className};
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * ${tableComment} Mapper
 *
 * @author ${author}
 * @date ${date}
 */
@Component
@DS("slave")
public interface ${className}AdminMapper extends BaseMapper<Admin${className}> {

}
