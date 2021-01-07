package com.example.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;
import java.util.stream.IntStream;

@Configuration
@EnableAuthorizationServer // 配置授权服务。
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    // 用来配置客户端详情服务
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 这里是第三方合作用户的客户id，秘钥的配置
        // 使用in-memory存储
        clients.inMemory()
                // client_id，用户账号
                .withClient("c1")
                // 客户端密钥
                .secret(new BCryptPasswordEncoder().encode("secret"))
                // 资源列表，资源标识
                .resourceIds("user")
                // 授权类型（4种）
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit",
                        "refresh_token")
                // 客戶端允许的授权范围
                .scopes("all")
                // false跳转到授权页面，让用户点击授权，如果是true,相当于自动点击授权，就不跳转授权页面
                .autoApprove(true)//
                // 加上验证回调地址，返回授权码信息
                .redirectUris("http://localhost:8083/auth/token.html");

        // 如果有多个用户，配置多个客户详情
        // .and().withClient()

    }

    /**
     * 使用jwt令牌
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("123");//jwt密码
        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        //token增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter()));
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);

        defaultTokenServices.setAccessTokenValiditySeconds(43200);//令牌有效期12小时
        defaultTokenServices.setRefreshTokenValiditySeconds(259200);//刷新令牌有效期3天
        return defaultTokenServices;
    }

    @Autowired
    // 认证管理
    private AuthenticationManager authenticationManager;


    @Override
    // 用来配置令牌（token）的访问端点
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager)
                // 令牌管理服务
                .tokenServices(tokenServices())
                .accessTokenConverter(accessTokenConverter())
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);// 允许post提交
    }

    @Override
    // 用来配置令牌端点的安全约束,拦截规则
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //实际测试这三个不配置也可以，具体作用未知
        security
                // 提供公有密匙的端点，如果你使用JWT令牌的话, 允许
                .tokenKeyAccess("permitAll()")
                // oauth/check_token：用于资源服务访问的令牌解析端点，允许
                .checkTokenAccess("permitAll()")
                // 表单认证,申请令牌
                .allowFormAuthenticationForClients();
    }

}