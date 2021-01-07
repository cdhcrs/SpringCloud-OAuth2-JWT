package com.example.info;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @RequestMapping("/user/info")
    public String userinfo(){
        return "success";
    }

    @RequestMapping("/admin/info")
    public String admininfo(){
        return "success";
    }
}
