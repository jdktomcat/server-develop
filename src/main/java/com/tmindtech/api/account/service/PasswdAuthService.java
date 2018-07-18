package com.tmindtech.api.account.service;

import com.google.common.base.Strings;
import com.tmindtech.api.account.Config;
import com.tmindtech.api.account.db.AccountMapper;
import com.tmindtech.api.account.db.PasswdAuthMapper;
import com.tmindtech.api.account.model.Account;
import com.tmindtech.api.account.model.PasswdAuth;
import com.tmindtech.api.base.exception.AwesomeException;
import java.sql.Timestamp;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by RexQian on 2017/7/13.
 */
@Service
public class PasswdAuthService {
    private static final int MIN_PASSWD_LENGTH = 6;
    private static final int MAX_PASSWD_LENGTH = 18;

    @Autowired
    private PasswdAuthMapper passwdAuthMapper;

    @Autowired
    private AccountMapper accountMapper;


    public Account getByUsernameAndPasswd(String username, String password) {
        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
            throw new AwesomeException(Config.ERROR_USERNAME_OR_PASSWD);
        }

        Account account = new Account();
        account.username = username;
        account = accountMapper.selectOne(account);
        if (account == null) {
            throw new AwesomeException(Config.ERROR_USER_NOT_FOUND);
        }
        PasswdAuth passwdAuth = passwdAuthMapper.selectByPrimaryKey(account.id);
        if (passwdAuth == null) {
            throw new AwesomeException(Config.ERROR_USERNAME_OR_PASSWD);
        }
        if (!BCrypt.checkpw(password, passwdAuth.password)) {
            throw new AwesomeException(Config.ERROR_USERNAME_OR_PASSWD);
        }
        return account;
    }

    @Transactional
    public void updatePasswdByUser(long id, String oldPassword, String password) {
        if (Strings.isNullOrEmpty(oldPassword) || oldPassword.length() < MIN_PASSWD_LENGTH
                || oldPassword.length() > MAX_PASSWD_LENGTH) {
            throw new AwesomeException(Config.ERROR_PASSWD_TOO_SHORT_OR_TOO_LONG);
        }
        Account account = accountMapper.selectByPrimaryKey(id);
        if (account == null) {
            throw new AwesomeException(Config.ERROR_USER_NOT_FOUND);
        }
        PasswdAuth passwdAuth = passwdAuthMapper.selectByPrimaryKey(account.id);
        if (!BCrypt.checkpw(oldPassword, passwdAuth.password)) {
            throw new AwesomeException(Config.ERROR_USERNAME_OR_PASSWD);
        }
        if (Strings.isNullOrEmpty(password) || password.length() < MIN_PASSWD_LENGTH
                || password.length() > MAX_PASSWD_LENGTH) {
            throw new AwesomeException(Config.ERROR_PASSWD_TOO_SHORT_OR_TOO_LONG);
        }
        passwdAuth.password = BCrypt.hashpw(password, BCrypt.gensalt());
        passwdAuth.modifyTime = Timestamp.from(Instant.now());
        passwdAuthMapper.updateByPrimaryKeySelective(passwdAuth);
    }

    @Transactional
    public void updatePasswdByAdmin(long id, String password) {
        Account account = accountMapper.selectByPrimaryKey(id);
        if (account == null) {
            throw new AwesomeException(Config.ERROR_USER_NOT_FOUND);
        }
        PasswdAuth passwdAuth = passwdAuthMapper.selectByPrimaryKey(account.id);
        if (Strings.isNullOrEmpty(password) || password.length() < MIN_PASSWD_LENGTH
                || password.length() > MAX_PASSWD_LENGTH) {
            throw new AwesomeException(Config.ERROR_PASSWD_TOO_SHORT_OR_TOO_LONG);
        }
        if (passwdAuth == null) {
            PasswdAuth newPwd = new PasswdAuth();
            newPwd.id = id;
            newPwd.password = BCrypt.hashpw(password, BCrypt.gensalt());
            passwdAuthMapper.insertSelective(newPwd);
            return;
        }
        passwdAuth.password = BCrypt.hashpw(password, BCrypt.gensalt());
        passwdAuth.modifyTime = Timestamp.from(Instant.now());
        passwdAuthMapper.updateByPrimaryKeySelective(passwdAuth);
    }
}
