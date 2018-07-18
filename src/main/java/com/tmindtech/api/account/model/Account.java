package com.tmindtech.api.account.model;

import java.sql.Timestamp;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Created by RexQian on 2017/2/9.
 */
@Table(name = "t_account")
public class Account {
    public static final String STATE_ENABLE = "enable";
    public static final String STATE_DISABLE = "disable";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String username;

    public String nickname;

    public String avatar;

    public String state;

    public Timestamp createTime;

    public Timestamp modifyTime;

}
