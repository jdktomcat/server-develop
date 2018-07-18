package com.tmindtech.api.account.model;

import java.sql.Timestamp;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by RexQian on 2017/7/13.
 */
@Table(name = "t_passwd_auth")
public class PasswdAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
    public String password;
    
    public Timestamp createTime;

    public Timestamp modifyTime;
}
