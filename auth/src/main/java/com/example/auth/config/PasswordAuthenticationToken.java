package com.example.auth.config;


import com.example.auth.entity.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PasswordAuthenticationToken extends AbstractAuthenticationToken {

    //用户信息
    private User user;

    public PasswordAuthenticationToken(String account, String password){
        super(null);
        user=new User();
        user.setUserAccount(account);
        user.setUserPassword(password);
        //标记未认证
        super.setAuthenticated(false);//注意这个构造方法是认证时使用的
    }

    public PasswordAuthenticationToken(User user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user=user;
        System.out.println(user);
        //标记已认证
        super.setAuthenticated(true);//注意这个构造方法是认证成功后使用的
    }

    public User getUser(){
        return this.user;
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
