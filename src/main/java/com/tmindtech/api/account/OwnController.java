package com.tmindtech.api.account;

import com.tmindtech.api.account.service.LoginService;
import com.tmindtech.api.base.annotation.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by RexQian on 2017/2/21.
 */
@RestController
@RequestMapping("/own")
@Auth
public class OwnController {

    @Autowired
    LoginService loginService;

    @GetMapping
    public Object getOwnInfo() {
        return loginService.getOwnInfo();
    }

    @DeleteMapping
    public void logout() {
        loginService.logout();
    }
}
