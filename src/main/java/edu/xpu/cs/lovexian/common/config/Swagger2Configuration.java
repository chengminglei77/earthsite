package edu.xpu.cs.lovexian.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
@Configuration
@EnableSwagger2
public class Swagger2Configuration {
    @Value( "${earthsite.swaggerShow.show}" )
    private boolean swaggerShow;

    @Bean
    public Docket createRestApi() {
        if(swaggerShow) {
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("edu.xpu.cs.lovexian"))
                    .paths(PathSelectors.any())
                    .build();
        }else {
            return new Docket( DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .select()
                    .apis( RequestHandlerSelectors.basePackage("edu.00.00.0.0"))
                    .paths( PathSelectors.any())
                    .build();
        }
    }

    private ApiInfo apiInfo() {
        if(swaggerShow) {
            return new ApiInfoBuilder()
                    .title("爱上西安 api文档")
                    .description("爱上西安api文档")
                    .version("1.0")
                    .build();
        }else {
            return new ApiInfoBuilder()
                    .title("项目生产阶段禁止使用swagger查看接口")
                    .description("爱上西安api文档")
                    .version("1.0")
                    .build();
        }
    }

}
