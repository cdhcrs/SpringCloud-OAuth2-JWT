package com.example.auth.config;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public class Provider {
    private ProviderStrategy providerStrategy;

    /**
     * 根据type选择provider
     * @param type
     */
    public Provider(String type){
        //默认密码登录，如果传入一个不存在的类型，就使用密码登录
        this.providerStrategy=new PasswordAuthenticationProvider();
        if("phone".equals(type)){
            this.providerStrategy=new PhoneAuthenticationProvider();
        }else if("email".equals(type)){
            this.providerStrategy=new EmailAuthenticationProvider();
        }
    }

    /**
     * 执行某provider的认证方法
     * @param request
     * @return
     */
    public Authentication executeStrategy(HttpServletRequest request){
        return providerStrategy.authenticate(request);
    }
}
