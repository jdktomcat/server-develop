package com.tmindtech.api.account.model;

/**
 * Created by RexQian on 2017/2/21.
 */
public class OAuth2Token {

    /*接口调用凭证*/
    public String accessToken;

    /*access_token接口调用凭证超时时间，单位（秒）*/
    public Long expiresIn;

    /*用户刷新access_token*/
    public String refreshToken;

    /*授权用户唯一标识*/
    public String openId;

    /*用户授权的作用域，使用逗号（,）分隔*/
    public String scope;
}
