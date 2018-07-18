package com.tmindtech.api.account.db;

import com.tmindtech.api.account.model.PermissionModel;
import com.tmindtech.api.account.model.RolePermission;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * Created by RexQian on 2017/2/22.
 */
public interface RolePermissionMapper extends Mapper<RolePermission> {
    List<PermissionModel> getPermissionsByRoleId(@Param("role_id") long roleId);
}
