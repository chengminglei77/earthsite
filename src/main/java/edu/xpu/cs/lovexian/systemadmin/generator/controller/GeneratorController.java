package edu.xpu.cs.lovexian.systemadmin.generator.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.xpu.cs.lovexian.common.annotation.ControllerEndpoint;
import edu.xpu.cs.lovexian.common.controller.BaseController;
import edu.xpu.cs.lovexian.common.domain.EarthSiteResponse;
import edu.xpu.cs.lovexian.common.domain.QueryRequest;
import edu.xpu.cs.lovexian.common.exception.EarthSiteException;
import edu.xpu.cs.lovexian.common.utils.FileUtil;
import edu.xpu.cs.lovexian.common.utils.LoveXianUtil;
import edu.xpu.cs.lovexian.systemadmin.generator.entity.Column;
import edu.xpu.cs.lovexian.systemadmin.generator.entity.GeneratorConfig;
import edu.xpu.cs.lovexian.systemadmin.generator.entity.GeneratorConstant;
import edu.xpu.cs.lovexian.systemadmin.generator.entity.Table;
import edu.xpu.cs.lovexian.systemadmin.generator.helper.GeneratorHelper;
import edu.xpu.cs.lovexian.systemadmin.generator.service.IGeneratorConfigService;
import edu.xpu.cs.lovexian.systemadmin.generator.service.IGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**                                                                                ____________________
      _                _                                                           < 神兽护体，永无bug! >
    | |__  _   _  ___| |__   ___ _ __   __ _ _ __   ___ _ __   __ _                --------------------
   | '_ \| | | |/ __| '_ \ / _ \ '_ \ / _` | '_ \ / _ \ '_ \ / _` |                       \   ^__^
  | | | | |_| | (__| | | |  __/ | | | (_| | |_) |  __/ | | | (_| |                        \  (oo)\_______
 |_| |_|\__,_|\___|_| |_|\___|_| |_|\__, | .__/ \___|_| |_|\__, |                           (__)\       )\/\
                                   |___/|_|                |___/                                ||----w |
                                                                                                ||     ||
 * @author huchengpeng
 */
@Slf4j
@RestController
@RequestMapping("/generator")
public class GeneratorController extends BaseController {

    private static final String SUFFIX = "_code.zip";

    String[] finishTableName = {"sys_user","sys_log"};

    @Autowired
    private IGeneratorService iGeneratorService;
    @Autowired
    private IGeneratorConfigService iGeneratorConfigService;
    @Autowired
    private GeneratorHelper generatorHelper;

    @GetMapping("tables/info")
    @RequiresPermissions("generator:view")
    public EarthSiteResponse tablesInfo(@RequestParam(defaultValue = "1") @NotBlank(message = "{required}")String dataSourceType, String tableName, QueryRequest request) {
        String dataSource = "";
        if("0".equals(dataSourceType)){
            dataSource = GeneratorConstant.DATABASE_NAME_RBAC;
        }else if("1".equals(dataSourceType)){
            dataSource = GeneratorConstant.DATABASE_NAME_MASTER;
        }
        IPage<Table> tables = iGeneratorService.getTables(tableName, request, GeneratorConstant.DATABASE_TYPE, dataSource);
        for (Table record : tables.getRecords()) {
            record.setDataSourceType(dataSourceType);
        }
        Map<String, Object> dataTable = getDataTable(tables);
        return EarthSiteResponse.SUCCESS().data(dataTable);
    }

    @GetMapping
    @RequiresPermissions("generator:generate")
    @ControllerEndpoint(exceptionMessage = "代码生成失败")
    public void generate(@NotBlank(message = "{required}")String dataSourceType,@NotBlank(message = "{required}") String name, String remark, HttpServletResponse response) throws Exception {
        String dataSource = "";
        if("0".equals(dataSourceType)){
            dataSource = GeneratorConstant.DATABASE_NAME_RBAC;
        }else if("1".equals(dataSourceType)){
            dataSource = GeneratorConstant.DATABASE_NAME_MASTER;
        }
        GeneratorConfig generatorConfig = iGeneratorConfigService.findGeneratorConfig();

        if (generatorConfig == null) {
            throw new EarthSiteException("代码生成配置为空");
        }

        String className = name;
        if (GeneratorConfig.TRIM_YES.equals(generatorConfig.getIsTrim())) {
            className = RegExUtils.replaceFirst(name, generatorConfig.getTrimValue(), StringUtils.EMPTY);
        }

        generatorConfig.setTableName(name);
//        generatorConfig.setClassName("Admin"+generatorConfig.getClassName());
        generatorConfig.setClassName(LoveXianUtil.underscoreToCamel(className));
        generatorConfig.setTableComment(remark);
        // 生成代码到临时目录
        List<Column> columns = iGeneratorService.getColumns(GeneratorConstant.DATABASE_TYPE, dataSource, name);

        generatorHelper.generateEntityFile(columns, generatorConfig);
        generatorHelper.generateMapperFile(columns, generatorConfig);
        generatorHelper.generateServiceFile(columns, generatorConfig);
        generatorHelper.generateServiceImplFile(columns, generatorConfig);
        generatorHelper.generateControllerFile(columns, generatorConfig);
        generatorHelper.type2JdbcType(columns);
        generatorHelper.generateMapperXmlFile(columns, generatorConfig);
        // 打包
        String zipFile = System.currentTimeMillis() + SUFFIX;
        FileUtil.compress(GeneratorConstant.TEMP_PATH + "src", zipFile);
        // 下载
        FileUtil.download(zipFile, name + SUFFIX, true, response);
        // 删除临时目录
        FileUtil.delete(GeneratorConstant.TEMP_PATH);
    }
}
