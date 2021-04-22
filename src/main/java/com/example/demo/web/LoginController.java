package com.example.demo.web;

import com.example.demo.MyConfigurationCondition;
import com.example.demo.service.UserService;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Lazy
@RestController
public class LoginController {
    public LoginController(){

    }
    @RequestMapping("/login")
    public String login(){
        System.out.println(this.getClass().getClassLoader());
        new UserService().insert();
        return "success";
    }



}
