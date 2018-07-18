package com.tmindtech.api.account.model;

import java.sql.Timestamp;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by RexQian on 2017/2/21.
 */
@Table(name = "t_oauth2_uaa")
public class OAuth2Uaa {

    @Id
    public Long id;

    /**
     * 第三方openId
     */
    public String openId;

    /**
     * 第三方接口调用凭证
     */
    public String accessToken;

    /**
     * access token失效时间
     */
    public Timestamp expiredTime;

    /**
     * 用于刷新access token
     */
    public String refreshToken;

    public Timestamp createTime;
    public Timestamp modifyTime;
}
