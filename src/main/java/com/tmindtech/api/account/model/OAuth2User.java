package com.tmindtech.api.account.model;

import com.google.common.base.Strings;

/**
 * OAuth2的用户信息 此处仅定义了可能使用到的字段
 * Created by RexQian on 2017/2/21.
 */
public class OAuth2User {

    /*用户Id*/
    public String openId;

    /*登录名*/
    public String account;

    /*用户真实姓名*/
    public String realName;

    /*头像地址：http://[sso4]/[ headpicture]  注意：该地址为外网能访问的地址*/
    public String imgAddr;

    /*昵称*/
    public String displayName;

    public String unionId;


    public static String getNickname(OAuth2User user) {
        if (!Strings.isNullOrEmpty(user.displayName)) {
            return user.displayName;
        } else if (!Strings.isNullOrEmpty(user.realName)) {
            return user.realName;
        } else if (!Strings.isNullOrEmpty(user.account)) {
            return user.account;
        } else {
            return null;
        }
    }
}
