package com.tmindtech.api.account.model;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by RexQian on 2017/2/22.
 */
@Table(name = "t_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    /**
     * 角色名
     */
    public String name;

    /**
     * 角色描述
     */
    public String description;

    public Timestamp createTime;

    public Timestamp modifyTime;

    @Transient
    public List<PermissionModel> permissions;
}
