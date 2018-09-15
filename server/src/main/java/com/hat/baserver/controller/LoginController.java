package com.hat.baserver.controller;

import com.hat.baserver.pojo.ReqVerifyLoginMobileInfo;
import com.hat.baserver.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/v1/login/op_verify_login_mobile.json")
    public Object verifyLoginMobile(@RequestBody ReqVerifyLoginMobileInfo loginMobileInfo){
        return loginService.verifyLoginMobile(loginMobileInfo);
    }

    @GetMapping(value = "/v1/login/op_verify_login_mobile.json")
    public Object getVerifyLoginMobile(ReqVerifyLoginMobileInfo loginMobileInfo){
        return loginService.verifyLoginMobile(loginMobileInfo);
    }
}
