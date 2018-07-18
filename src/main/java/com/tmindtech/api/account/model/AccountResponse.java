package com.tmindtech.api.account.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * 账户详情
 *
 * @author lwtang
 * @date 2017-11-03
 */
public class AccountResponse {
    public Long id;
    public String username;
    public String nickname;
    public String avatar;
    public String state;
    public String socketAuth;
    public List<Long> permissionIds;
    public Timestamp createTime;
    public Timestamp modifyTime;
    public List<Role> roles;
}
