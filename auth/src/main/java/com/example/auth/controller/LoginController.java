package com.example.auth.controller;


import com.example.common.utils.R;
import com.example.auth.entity.User;
import com.example.auth.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register")
    public R register(User user){
        if(!userService.save(user)){
            return R.error("注册失败");
        }
        return R.success();
    }

    @GetMapping("/noLogin")
    public R noLogin(){
        return R.error("未登录");
    }

    @GetMapping("/success")
    public R success(){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //脱敏
        user.setUserPassword(null);
        return R.success(user);
    }

    @GetMapping("/failure")
    public R failure(){
        return R.error("登录失败");
    }

    @GetMapping("/getPhoneCode")
    public R getPhoneCode(HttpServletRequest request){

        //保存验证码到session
        request.getSession().setAttribute("phoneCode","1234");
        return R.success();
    }

    @GetMapping("/getEmailCode")
    public R getEmailCode(HttpServletRequest request){
        //保存验证码到session
        request.getSession().setAttribute("emailCode","1234");
        return R.success();
    }
}
