package com.tmindtech.api.account;


import com.tmindtech.api.account.model.Account;
import com.tmindtech.api.account.model.PasswdLoginRequest;
import com.tmindtech.api.account.model.SetPasswdRequest;
import com.tmindtech.api.account.service.LoginService;
import com.tmindtech.api.account.service.PasswdAuthService;
import com.tmindtech.api.account.service.PermissionService;
import com.tmindtech.api.base.annotation.Auth;
import com.tmindtech.api.base.annotation.Permission;
import com.tmindtech.api.base.auth.AuthSession;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by RexQian on 2017/7/12.
 */
@RestController
public class PasswdAuthController {
    @Autowired
    LoginService loginService;

    @Autowired
    PasswdAuthService passwdAuthService;

    @Autowired
    AuthSession authSession;

    @Autowired
    PermissionService permissionService;



    @PostMapping("/login/passwd")
    public Object loginByPasswd(HttpServletRequest request, @RequestBody PasswdLoginRequest req) {
        Account account = passwdAuthService.getByUsernameAndPasswd(req.username, req.password);
        return loginService.login(account.id);
    }



    @PutMapping("/passwd/users/{id}")
    @Auth
    public void updatePasswdByUser(@PathVariable long id, @RequestBody SetPasswdRequest req) {
        if (authSession.getAuthId() != id) {
            permissionService.check(Config.Pm.MANAGE_ACCOUNT);
        }
        passwdAuthService.updatePasswdByUser(id, req.oldPassword, req.password);
    }

    @PutMapping("/passwd/admin/{id}")
    @Auth({@Permission(Config.Pm.MANAGE_ACCOUNT)})
    public void updatePasswdByAdmin(@PathVariable long id, @RequestBody SetPasswdRequest req) {
        passwdAuthService.updatePasswdByAdmin(id, req.password);
    }
}
