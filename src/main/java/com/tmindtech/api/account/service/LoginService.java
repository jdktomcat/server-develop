package com.tmindtech.api.account.service;

import com.tmindtech.api.account.Config;
import com.tmindtech.api.account.db.AccountMapper;
import com.tmindtech.api.account.model.Account;
import com.tmindtech.api.account.model.AccountResponse;
import com.tmindtech.api.base.auth.AuthSession;
import com.tmindtech.api.base.exception.AwesomeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by RexQian on 2017/4/21.
 */
@Service
public class LoginService {

    @Autowired
    AuthSession authSession;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    RoleService roleService;


    public AccountResponse login(long accountId) {
        Account account = accountService.getById(accountId);
        if (!account.state.equals(Account.STATE_ENABLE)) {
            throw new AwesomeException(Config.ERROR_USER_DISABLE);
        }

        authSession.setAuth(account.id);
        return getOwnInfo();
    }

    public void logout() {
        authSession.removeAuth();
    }

    public AccountResponse getOwnInfo() {
        long id = authSession.getAuthId();
        Account account = accountService.getById(id);

        AccountResponse response = new AccountResponse();
        response.id = id;
        response.username = account.username;
        response.nickname = account.nickname;
        response.state = account.state;
        response.createTime = account.createTime;
        response.modifyTime = account.modifyTime;
        response.roles = roleService.getListByAccount(id);
        response.permissionIds = accountMapper.getPermissionIds(id);
        return response;
    }
}
