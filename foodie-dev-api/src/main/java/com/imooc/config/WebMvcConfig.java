package com.imooc.config;

import com.imooc.controller.interceptor.UserTokenInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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

    /**
     * 将拦截器注入
     * @param
     */

    public UserTokenInterceptor userTokenInterceptor(){
        return  new UserTokenInterceptor();
    }

    /**
     * 注册拦截器
     * @param registry
     * registry.addInterceptor(userTokenInterceptor())
     *                 .addPathPatterns("/hello")
     *                 .addPathPatterns("/shopcart/add")
     *                 .addPathPatterns("/shopcart/del")
     *                 .addPathPatterns("/address/list")
     *                 .addPathPatterns("/address/add")
     *                 .addPathPatterns("/address/update")
     *                 .addPathPatterns("/address/setDefalut")
     *                 .addPathPatterns("/address/delete")
     *                 .addPathPatterns("/orders/*")
     *                 .addPathPatterns("/center/*")
     *                 .addPathPatterns("/userInfo/*")
     *                 .addPathPatterns("/myorders/*")
     *                 .addPathPatterns("/mycomments/*")
     *                 .excludePathPatterns("/myorders/deliver")
     *                 .excludePathPatterns("/orders/notifyMerchantOrderPaid");
     *                 首先： *代表所有的路径，excludePathPatterns代表这个路径不被拦截
     *
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns表示添加拦截的路径
        registry.addInterceptor(userTokenInterceptor()).addPathPatterns("/indexsss/hellos");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
