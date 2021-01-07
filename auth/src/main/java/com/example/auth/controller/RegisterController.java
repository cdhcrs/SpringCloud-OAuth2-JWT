package com.example.auth.controller;

import com.example.auth.entity.User;
import com.example.auth.service.impl.UserServiceImpl;
import com.example.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register")
    public R register(User user){
        user.setUserRole("user");
        //密码加密
        user.setUserPassword(new BCryptPasswordEncoder().encode(user.getUserPassword()));
        if(userService.save(user)){
            return R.success();
        }
        return R.error("注册失败");
    }
}
