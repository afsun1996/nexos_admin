package com.nexos.nexos_admin.config;    /**
 * Created by 孙爱飞 on 2020/3/28.
 */

import com.nexos.nexos_admin.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 *@description: //TODO
 *@author: your name
 *@create: 2020-03-28 16:44
 *@version: 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

//    //所有的WebMvcConfigurerAdapter组件都会一起起作用
//    @Bean //将组件注册在容器中
//    public WebMvcConfigurer webMvcConfigurerAdapter(HttpRequestInterceptor httpRequestInterceptor){
//        return new WebMvcConfigurer(){
//
//            //注册拦截器
//            @Override
//            public void addInterceptors(InterceptorRegistry registry) {
//                // /**  表示拦截所有路径下的所有请求
//                registry.addInterceptor(httpRequestInterceptor)
//                        .addPathPatterns("/**");
//            }
//        };
//    }

    @Bean
    public Docket createRestApi() {
        //添加header参数
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar.name(Constant.AUTH_HEAD).description("user token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build(); //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数


        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.nexos.nexos_admin.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("STORY-ADMIN API")
                .description("STORY-ADMIN's REST API")//详细描述
                .version("1.0")
                .contact(new Contact("sunnj", "http://www.sundayfine.com", "sunnj87@163.com"))//作者
//                .license("The Apache License, Version 2.0")//许可证信息
//                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")//许可证地址
                .build();
    }

//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//
//    }

}