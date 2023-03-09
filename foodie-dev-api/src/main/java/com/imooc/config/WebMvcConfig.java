package com.imooc.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 用于服务资源映射加载图片
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 实现静态资源的映射
    // 映射完以后，images目录下所有的资源都可以访问到(要从images下级目录开始写url地址)
    // 访问的地址就为：localhost:8088/foodie/faces/2211097A108A4PH0/face-2211097A108A4PH0.jpg
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")     // 注册映射所有资源
                .addResourceLocations("classpath:/META-INF/resources/")  // 映射swagger2
                .addResourceLocations("file:/workspaces/images/");  // 映射本地静态资源
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
