package com.tmindtech.api.account.model;

import java.util.List;

/**
 * @author lwtang
 * @date 2017-11-03
 */
public class AddRole {
    public String name ;
    public String description;
    public List<Long> permissionIds;
}
