package com.tmindtech.api.account;

import com.tmindtech.api.account.model.AddRole;
import com.tmindtech.api.account.model.Role;
import com.tmindtech.api.account.service.RoleService;
import com.tmindtech.api.base.annotation.Auth;
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
 * Created by RexQian on 2017/2/22.
 */
@RestController
@RequestMapping("/roles")
@Auth({@Permission(Config.Pm.MANAGE_ACCOUNT)})
public class RoleController {
    @Autowired
    RoleService roleService;

    @GetMapping
    public Object getList() throws AwesomeException {
        List<Role> list = roleService.getList();
        DataList dataList = new DataList<Role>(0, list.size(), list.size(), list);
        return dataList;
    }

    @PostMapping
    public Role addRole(@RequestBody AddRole req) throws AwesomeException {
        return roleService.addRole(req.name, req.description, req.permissionIds);
    }

    @GetMapping("/{id}")
    public Role getRole(@PathVariable long id) {
        return roleService.getDetail(id);
    }

    @PutMapping("/{id}")
    public void updateRole(@PathVariable long id, @RequestBody AddRole req) throws AwesomeException {
        roleService.updateRole(id, req.name, req.description, req.permissionIds);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable long id) throws AwesomeException {
        roleService.deleteRole(id);
    }
}
