package com.example.auth.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//filter用于接收网络请求，调用manager进行认证
//manager管理多个provider，选择合适的provider进行认证
//provider负责认证，返回token
//token存储认证信息
public class AuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    protected AuthenticationProcessingFilter() {
        super("/login");//认证url
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String type=request.getParameter("type");
        //策略模式
        Provider provider=new Provider(type);
        return getAuthenticationManager().authenticate(provider.executeStrategy(request));
    }
}
