package com.example.auth.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auth.entity.User;
import com.example.auth.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class PasswordAuthenticationProvider implements AuthenticationProvider,ProviderStrategy {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //认证代码，认证通过返回认证对象，失败返回null
        PasswordAuthenticationToken token = (PasswordAuthenticationToken) authentication;
        if(token.getUser()==null) return null;
        //验证账号密码
        User user=userService.getOne(new QueryWrapper<User>().eq("user_account",token.getUser().getUserAccount())
                /*.eq("user_password",token.getUser().getUserPassword())*/,false);
        System.out.println(user);

        if(user!=null){
            //加密密码的比较
            if(!passwordEncoder.matches(token.getUser().getUserPassword(),user.getUserPassword())){
                return null;
            }
            //授予用户权限
            String role;
            if(user.getUserRole().equals("user")){
                role="ROLE_USER";
            }else{
                role="ROLE_ADMIN";//管理员权限
            }
            //写入用户信息并返回认证类
            return new PasswordAuthenticationToken(user,
                    Arrays.asList(new SimpleGrantedAuthority(role)));
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        //Manager传递token给provider，调用本方法判断该provider是否支持该token。不支持则尝试下一个filter
        //本类支持的token类：UserPasswordAuthenticationToken
        return (PasswordAuthenticationToken.class.isAssignableFrom(aClass));
    }

    @Override
    public Authentication authenticate(HttpServletRequest request) {
        String user = request.getParameter("username");
        String password = request.getParameter("password");
        return new PasswordAuthenticationToken(user,password);
    }
}
