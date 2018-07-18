package com.tmindtech.api.account.model;

import java.sql.Timestamp;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by RexQian on 2017/2/22.
 */
@Table(name = "t_permission")
public class PermissionModel {
    @Id
    public Long id;

    /**
     * 权限名
     */
    public String name;

    /**
     * 权限描述
     */
    public String description;

    public Timestamp createTime;

    public Timestamp modifyTime;
}
