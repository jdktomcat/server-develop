package com.tmindtech.api.account.model;

import java.sql.Timestamp;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by RexQian on 2017/7/5.
 */
@Table(name = "t_account_role")
public class AccountRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long accountId;

    public Long roleId;

    public Timestamp createTime;

    public Timestamp modifyTime;
}
