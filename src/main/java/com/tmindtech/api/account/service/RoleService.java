package com.tmindtech.api.account.service;

import com.google.common.base.Strings;
import com.tmindtech.api.account.Config;
import com.tmindtech.api.account.db.AccountMapper;
import com.tmindtech.api.account.db.AccountRoleMapper;
import com.tmindtech.api.account.db.PermissionMapper;
import com.tmindtech.api.account.db.RoleMapper;
import com.tmindtech.api.account.db.RolePermissionMapper;
import com.tmindtech.api.account.model.Account;
import com.tmindtech.api.account.model.AccountRole;
import com.tmindtech.api.account.model.PermissionModel;
import com.tmindtech.api.account.model.Role;
import com.tmindtech.api.account.model.RolePermission;
import com.tmindtech.api.base.exception.AwesomeException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by RexQian on 2017/2/22.
 */
@Service
public class RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    AccountRoleMapper accountRoleMapper;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    RolePermissionMapper rolePermissionMapper;

    @Transactional
    public void addRole(Long accountId, long roleId) {
        Account account = accountMapper.selectByPrimaryKey(accountId);
        if (account == null) {
            throw new AwesomeException(Config.ERROR_USER_NOT_FOUND);
        }

        Role role = roleMapper.selectByPrimaryKey(roleId);
        if (role == null) {
            throw new AwesomeException(Config.ERROR_ROLE_NOT_FOUND);
        }
        AccountRole accountRole = new AccountRole();
        accountRole.accountId = accountId;
        accountRole.roleId = roleId;
        if (accountRoleMapper.selectCount(accountRole) > 0) {
            // role already exist
            // ignore this operation
            return;
        }
        accountRoleMapper.insertSelective(accountRole);
    }

    @Transactional
    public void setRoles(long accountId, List<Long> roleIds) throws AwesomeException {
        Account account = accountMapper.selectByPrimaryKey(accountId);
        if (account == null) {
            throw new AwesomeException(Config.ERROR_USER_NOT_FOUND);
        }

        //check roles
        if (roleIds != null) {
            for (Long id : roleIds) {
                Role role = roleMapper.selectByPrimaryKey(id);
                if (role == null) {
                    throw new AwesomeException(Config.ERROR_ROLE_NOT_FOUND);
                }
            }
        }

        AccountRole accountRole = new AccountRole();
        accountRole.accountId = accountId;
        accountRoleMapper.delete(accountRole);

        if (roleIds != null) {
            for (Long roleId : roleIds) {
                addRole(accountId, roleId);
            }
        }
    }

    public List<Role> getListByAccount(long accountId) {
        List<Role> list = accountRoleMapper.getRoleList(accountId);
        for (Role item : list) {
            item.permissions = rolePermissionMapper.getPermissionsByRoleId(item.id);
        }
        return list;
    }

    public List<Role> getList() {
        List<Role> list = roleMapper.selectAll();
        list.remove(0);
        for (Role item : list) {
            item.permissions = rolePermissionMapper.getPermissionsByRoleId(item.id);
        }
        return list;
    }

    public Role get(Long id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    public Role getDetail(Long id) {
        Role role = checkRole(id);
        role.permissions = rolePermissionMapper.getPermissionsByRoleId(id);
        return role;
    }

    @SuppressWarnings("checkstyle:OverloadMethodsDeclarationOrder")
    @Transactional
    public Role addRole(String name, String description, List<Long> permissionIds) throws AwesomeException {
        Role role = addRole(name, description);
        setPermissions(role.id, permissionIds);
        role.permissions = rolePermissionMapper.getPermissionsByRoleId(role.id);
        return role;
    }

    @SuppressWarnings("checkstyle:OverloadMethodsDeclarationOrder")
    @Transactional
    public Role addRole(String name, String description) throws AwesomeException {
        checkRoleName(null, name);

        Role role = new Role();
        role.name = name;
        role.description = description;
        roleMapper.insertSelective(role);
        return role;
    }

    @Transactional
    public void updateRole(long id, String name, String description,
                           List<Long> permissionIds) throws AwesomeException {
        updateRole(id, name, description);
        setPermissions(id, permissionIds);
    }

    public void updateRole(long id, String name, String description) throws AwesomeException {
        checkRole(id);
        checkRoleName(id, name);
        Role role = new Role();
        role.id = id;
        role.name = name;
        role.description = Strings.nullToEmpty(description);
        role.modifyTime = Timestamp.from(Instant.now());
        int count = roleMapper.updateByPrimaryKeySelective(role);
        if (count == 0) {
            throw new AwesomeException(Config.ERROR_ROLE_NOT_FOUND);
        }
    }

    @Transactional
    public void deleteRole(long id) throws AwesomeException {
        Role role = roleMapper.selectByPrimaryKey(id);
        if (role == null) {
            throw new AwesomeException(Config.ERROR_ROLE_NOT_FOUND);
        }
        AccountRole accountRole = new AccountRole();
        accountRole.roleId = id;
        List<AccountRole> list = accountRoleMapper.select(accountRole);
        if (list.size() != 0) {
            throw new AwesomeException(Config.ERROR_ROLE_HAS_BEEN_USED);
        }
        roleMapper.deleteByPrimaryKey(id);

        RolePermission rolePermission = new RolePermission();
        rolePermission.roleId = id;
        rolePermissionMapper.delete(rolePermission);
    }

    private void checkRoleName(Long id, String name) throws AwesomeException {
        if (Strings.isNullOrEmpty(name)) {
            throw new AwesomeException(Config.ERROR_EMPTY_ROLE_NAME);
        }

        if (id != null) {
            Role role = roleMapper.selectByPrimaryKey(id);
            if (name.equals(role.name)) {
                return;
            }
        }

        Role role = new Role();
        role.name = name;
        if (roleMapper.selectCount(role) > 0) {
            throw new AwesomeException(Config.ERROR_DUPLICATE_ROLE_NAME);
        }
    }

    public void addPermission(long roleId, long permissionId) throws AwesomeException {
        checkRole(roleId);
        checkPermission(permissionId);

        RolePermission rolePermission = new RolePermission();
        rolePermission.roleId = roleId;
        rolePermission.permissionId = permissionId;
        if (rolePermissionMapper.selectCount(rolePermission) > 0) {
            // ignore if permission already exist
            return;
        }

        rolePermissionMapper.insertSelective(rolePermission);
    }

    private void setPermissions(long roleId, List<Long> permissionList) throws AwesomeException {
        // remove all  permission
        RolePermission rolePermission = new RolePermission();
        rolePermission.roleId = roleId;
        rolePermissionMapper.delete(rolePermission);

        // add permission
        if (permissionList != null) {
            for (long pmId : permissionList) {
                addPermission(roleId, pmId);
            }
        }
    }

    private Role checkRole(long roleId) throws AwesomeException {
        Role role = roleMapper.selectByPrimaryKey(roleId);
        if (role == null) {
            throw new AwesomeException(Config.ERROR_ROLE_NOT_FOUND);
        }
        return role;
    }

    private PermissionModel checkPermission(long permissionId) throws AwesomeException {
        PermissionModel permission = permissionMapper.selectByPrimaryKey(permissionId);
        if (permission == null) {
            throw new AwesomeException(Config.ERROR_PERMISSION_NOT_FOUND);
        }
        return permission;
    }
}
