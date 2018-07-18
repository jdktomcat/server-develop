package com.tmindtech.api.account;

import com.tmindtech.api.account.model.PermissionModel;
import com.tmindtech.api.account.service.PermissionService;
import com.tmindtech.api.base.annotation.Auth;
import com.tmindtech.api.base.annotation.Permission;
import com.tmindtech.api.base.model.DataList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by RexQian on 2017/2/23.
 */
@RestController
@RequestMapping("/permissions")
@Auth({@Permission(Config.Pm.MANAGE_ACCOUNT)})
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    /**
     * 获取权限信息列表
     */
    @GetMapping
    public Object getList() {
        List<PermissionModel> list = permissionService.getList();
        DataList dataList = new DataList<PermissionModel>(0, list.size(), list.size(), list);
        return dataList;
    }
}
