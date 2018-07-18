package com.tmindtech.api.account;

import com.github.pagehelper.Page;
import com.tmindtech.api.account.model.Account;
import com.tmindtech.api.account.model.AccountResult;
import com.tmindtech.api.account.model.Role;
import com.tmindtech.api.account.service.AccountService;
import com.tmindtech.api.account.service.RoleService;
import com.tmindtech.api.base.annotation.Auth;
import com.tmindtech.api.base.annotation.AwesomeParam;
import com.tmindtech.api.base.annotation.Permission;
import com.tmindtech.api.base.exception.AwesomeException;
import com.tmindtech.api.base.model.DataList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author RexQian
 * @date 2017/2/9
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    RoleService roleService;


    /**
     * 查询账户列表
     *
     * @return 账户列表
     */
    @GetMapping
    @Auth({@Permission(Config.Pm.MANAGE_ACCOUNT)})
    public Object getList(
            @AwesomeParam(required = false) Long roleId,
            @AwesomeParam(required = false) String nameLike,
            @AwesomeParam(defaultValue = "0") int offset,
            @AwesomeParam(defaultValue = "100") int limit) {
        Page<AccountResult> page = accountService.getList(roleId, nameLike, offset, limit);
        return new DataList<>(page);
    }

    @PostMapping
    @Auth({@Permission(Config.Pm.MANAGE_ACCOUNT)})
    public Account createAccount(@RequestBody Account account) {
        return accountService.create(account);
    }

    @GetMapping("/{id}")
    @Auth({@Permission(Config.Pm.MANAGE_ACCOUNT)})
    public Account getInfo(@PathVariable long id) {
        Account account = accountService.getById(id);
        if (account == null) {
            throw new AwesomeException(Config.ERROR_USER_NOT_FOUND);
        }
        return account;
    }

    @PutMapping("/{id}")
    @Auth({@Permission(Config.Pm.MANAGE_ACCOUNT)})
    public void updateAccountByAdmin(@PathVariable long id, @RequestBody Account account) {
        accountService.updateAccountByAdmin(id, account);
    }

    @PutMapping("/user/{id}")
    @Auth
    public void updateAccountByUser(@PathVariable long id, @RequestBody Account account) {
        accountService.updateAccountByUser(id, account);
    }

    @DeleteMapping("/{id}")
    @Auth({@Permission(Config.Pm.MANAGE_ACCOUNT)})
    public void deleteAccount(@PathVariable long id) {
        accountService.deleteAccount(id);
    }

    @GetMapping("/{id}/roles")
    @Auth({@Permission(Config.Pm.MANAGE_ACCOUNT)})
    public List<Role> getRoles(@PathVariable long id) {
        return roleService.getListByAccount(id);
    }
}
