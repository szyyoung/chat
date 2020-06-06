package com.example.restful.controller;

import com.example.common.dto.Response;
import com.example.common.dto.request.LoginDto;
import com.example.common.dto.request.RegisterDto;
import com.example.restful.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public Response register(@RequestBody RegisterDto registerDto, HttpServletRequest httpServletRequest) {
        return userService.register(registerDto, httpServletRequest);
    }


    @PostMapping("login")
    public Response login(@RequestBody LoginDto loginDto, HttpServletRequest httpServletRequest) {
        return userService.login(loginDto, httpServletRequest);
    }


    @PostMapping("select")
    public Response select(HttpServletRequest httpServletRequest) {
        return userService.select(httpServletRequest);
    }


}
