package com.example.auth.config;

import com.example.auth.entity.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class EmailAuthenticationToken extends AbstractAuthenticationToken {

    //用户信息
    private User user;
    private String email;
    private String code;
    private String realCode;

    public EmailAuthenticationToken(String email, String code, String realCode){
        super(null);
        this.email=email;
        this.code=code;
        this.realCode=realCode;
        //标记未认证
        super.setAuthenticated(false);//注意这个构造方法是认证时使用的
    }

    public EmailAuthenticationToken(User user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user=user;
        System.out.println(user);
        //标记已认证
        super.setAuthenticated(true);//注意这个构造方法是认证成功后使用的
    }

    public User getUser(){
        return this.user;
    }

    public String getEmail(){
        return this.email;
    }

    public String getCode(){
        return this.code;
    }

    public String getRealCode(){
        return this.realCode;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * 获取用户信息
     * @return
     */
    @Override
    public Object getPrincipal() {
        return user;
    }

}
