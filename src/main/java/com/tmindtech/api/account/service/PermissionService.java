package com.tmindtech.api.account.service;

import com.tmindtech.api.account.Config;
import com.tmindtech.api.account.db.AccountRoleMapper;
import com.tmindtech.api.account.db.PermissionMapper;
import com.tmindtech.api.account.db.RolePermissionMapper;
import com.tmindtech.api.account.model.AccountRole;
import com.tmindtech.api.account.model.PermissionModel;
import com.tmindtech.api.account.model.RolePermission;
import com.tmindtech.api.base.annotation.Permission;
import com.tmindtech.api.base.aop.AuthAspect;
import com.tmindtech.api.base.auth.AuthSession;
import com.tmindtech.api.base.exception.AwesomeException;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by RexQian on 2017/2/23.
 */
@Service
public class PermissionService implements AuthAspect.PermissionCheckCallback {


    @Autowired
    AuthSession authSession;

    @Autowired
    RolePermissionMapper rolePermissionMapper;

    @Autowired
    AccountRoleMapper accountRoleMapper;

    @Autowired
    AuthAspect authAspect;

    @Autowired
    PermissionMapper permissionMapper;

    @PostConstruct
    public void init() {
        authAspect.setPermissionCheckCallback(this);
    }

    public boolean hasPermission(long permissionId) {
        long uid = authSession.getAuthId();
        return hasPermission(uid, permissionId);
    }

    public boolean hasPermission(long uid, long permissionId) {
        if (uid == Config.SUPER_MAN_ID) {
            return true;
        }

        AccountRole accountRole = new AccountRole();
        accountRole.accountId = uid;
        List<AccountRole> roleList = accountRoleMapper.select(accountRole);
        for (AccountRole role : roleList) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.roleId = role.roleId;
            rolePermission.permissionId = permissionId;

            if (rolePermissionMapper.selectCount(rolePermission) > 0) {
                return true;
            }
        }
        return false;
    }

    public void permissionDeny() throws AwesomeException {
        throw new AwesomeException(Config.ERROR_PERMISSION_DENY);
    }

    public List<PermissionModel> getList() {
        return permissionMapper.selectAll();
    }

    public void check(Long... permissions) throws AwesomeException {
        for (Long permission : permissions) {
            if (hasPermission(permission)) {
                return;
            }
        }
        permissionDeny();
    }

    @Override
    public boolean check(Permission[] permissions) throws AwesomeException {
        for (Permission permission : permissions) {
            boolean grant = true;
            for (long value : permission.value()) {
                if (!hasPermission(value)) {
                    grant = false;
                    break;
                }
            }
            if (grant) {
                return true;
            }
        }
        return false;
    }
}
