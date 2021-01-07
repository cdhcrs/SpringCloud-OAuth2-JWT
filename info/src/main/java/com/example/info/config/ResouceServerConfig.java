package com.example.info.config;

import com.example.common.utils.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResouceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * 配置JWT
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123"); // 对称秘钥，资源服务器使用该秘钥来验证
        return converter;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                // 资源id，与认证服务器对应，如果资源id不一致会认证失败
                .resourceId("user")
                //                // 令牌服务
                //                .tokenServices(tokenService())
                // 令牌服务
                .tokenStore(tokenStore())
                .stateless(true)
                //认证异常
                .authenticationEntryPoint((httpServletRequest, httpServletResponse, e) -> {
                    ObjectMapper objectMapper=new ObjectMapper();
                    httpServletResponse.setContentType("application/json;charset=UTF-8");
                    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(R.error("认证异常")));
                })
                //无权访问
                .accessDeniedHandler((httpServletRequest, httpServletResponse, e) -> {
                    ObjectMapper objectMapper=new ObjectMapper();
                    httpServletResponse.setContentType("application/json;charset=UTF-8");
                    httpServletResponse.getWriter().write(objectMapper.writeValueAsString(R.error("无权访问")));
                });
    }

    /**
     * 配置拦截路径、权限等
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
            // 所有的访问,授权访问都要是all,和认证服务器的授权范围一一对应
            //.access("#oauth2.hasScope('all')")
            //去掉防跨域攻击
            .and().csrf().disable();
            //session管理
            /*.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/
    }

}
