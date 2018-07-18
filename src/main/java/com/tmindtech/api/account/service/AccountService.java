package com.tmindtech.api.account.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import com.tmindtech.api.account.Config;
import com.tmindtech.api.account.db.AccountMapper;
import com.tmindtech.api.account.db.AccountRoleMapper;
import com.tmindtech.api.account.model.Account;
import com.tmindtech.api.account.model.AccountResult;
import com.tmindtech.api.account.model.AccountRole;
import com.tmindtech.api.base.auth.AuthSession;
import com.tmindtech.api.base.exception.AwesomeException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by RexQian on 2017/2/21.
 */
@Service
public class AccountService {

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswdAuthService passwdAuthService;

    @Autowired
    AuthSession authSession;

    @Autowired
    AccountRoleMapper accountRoleMapper;

    @Autowired
    PermissionService permissionService;


    public Account getById(long id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    public Page<AccountResult> getList(Long roleId, String nameLike, int offset, int limit) {
        PageHelper.offsetPage(offset, limit);
        List<AccountResult> list = accountMapper.getAccounts(roleId, nameLike);
        for (AccountResult accountResult : list) {
            accountResult.roles = roleService.getListByAccount(accountResult.id);
        }
        return (Page<AccountResult>) list;
    }

    public Account create(String nickname, String avatar) {
        return create(UUID.randomUUID().toString(), nickname, avatar);
    }

    @Transactional
    public Account create(Account account) {
        if (Strings.isNullOrEmpty(account.username)) {
            throw new AwesomeException(Config.ERROR_INVALID_PARAM.format("用户名不能为空"));
        }
        Account select = new Account();
        select.username = account.username;
        if (accountMapper.selectCount(select) > 0) {
            throw new AwesomeException(Config.ERROR_INVALID_PARAM.format("用户名已存在"));
        }
        account.nickname = Strings.isNullOrEmpty(account.nickname) ? "" : account.nickname;
        account.state = account.state == null ? "enable" : account.state;
        accountMapper.insertSelective(account);
        passwdAuthService.updatePasswdByAdmin(account.id, "123456");
        roleService.addRole(account.id, Config.Role.DEVELOPER);
        return accountMapper.selectByPrimaryKey(account.id);
    }

    @Transactional
    public Account create(String username, String nickname, String avatar) {
        if (Strings.isNullOrEmpty(username)) {
            throw new AwesomeException(Config.ERROR_INVALID_PARAM.format("用户名不能为空"));
        }
        Account select = new Account();
        select.username = username;
        if (accountMapper.selectCount(select) > 0) {
            throw new AwesomeException(Config.ERROR_INVALID_PARAM.format("用户名已存在"));
        }

        Account account = new Account();

        account.nickname = Strings.isNullOrEmpty(nickname) ? "" : nickname;
        account.username = username;
        account.avatar = avatar;
        accountMapper.insertSelective(account);

        if (Strings.isNullOrEmpty(nickname)) {
            Account update = new Account();
            update.id = account.id;
            update.nickname = String.format("游客%06d", account.id);
            accountMapper.updateByPrimaryKeySelective(update);
        }


        return accountMapper.selectByPrimaryKey(account.id);
    }

    @Transactional
    public void updateAccountByAdmin(long id, Account account) {
        account.id = id;
        Account newAccount = accountMapper.selectByPrimaryKey(id);
        if (account.username != null) {
            Account select = new Account();
            select.username = account.username;
            select = accountMapper.selectOne(select);
            if (select != null && !select.username.equals(newAccount.username)) {
                throw new AwesomeException(Config.ERROR_INVALID_PARAM.format("用户名已存在"));
            }
        }
        account.modifyTime = Timestamp.from(Instant.now());
        accountMapper.updateByPrimaryKeySelective(account);
    }

    @Transactional
    public void updateAccountByUser(long id, Account account) {
        if (authSession.getAuthId() != id) {
            permissionService.permissionDeny();
        }
        account.id = id;
        account.modifyTime = Timestamp.from(Instant.now());
        accountMapper.updateByPrimaryKeySelective(account);
    }

    @Transactional
    public void deleteAccount(long id) {
        if (Config.SUPER_MAN_ID == id || authSession.getAuthId() == id) {
            throw new AwesomeException(Config.ERROR_DELETE_SUPER_OR_ITSELF_ACCOUNT);
        }
        AccountRole accountRole = new AccountRole();
        accountRole.accountId = id;
        accountRoleMapper.delete(accountRole);
        accountMapper.deleteByPrimaryKey(id);
    }

}
