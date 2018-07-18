package com.tmindtech.api.account;

import com.tmindtech.api.account.service.LoginService;
import com.tmindtech.api.base.annotation.Debug;
import com.tmindtech.api.base.exception.AwesomeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 超级管理员的登录接口
 * !!! 注意 !!!
 * 此接口必须严格控制访问
 *
 * @author RexQian
 * @date 2017/2/22
 */
@RestController
@RequestMapping("/login/super")
@Debug
public class SuperAuthController {

    @Autowired
    LoginService loginService;

    @PostMapping
    public Object login() throws AwesomeException {
        return loginService.login(Config.SUPER_MAN_ID);
    }

}
