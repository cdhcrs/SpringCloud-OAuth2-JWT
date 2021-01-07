package com.example.info;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册跨域请求拦截器
        registry.
                addInterceptor(new CorsInterceptor())
                .addPathPatterns("/**");//所有路径都被拦截
    }

}
