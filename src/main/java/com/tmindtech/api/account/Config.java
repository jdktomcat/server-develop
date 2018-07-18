package com.tmindtech.api.account;

import com.tmindtech.api.base.exception.ErrorCode;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by RexQian on 2017/2/21.
 */
public class Config {

    /**
     * 模块编号
     */
    public static final int MODEL_CODE = 2;

    /**
     * 错误定义
     */
    public static final ErrorCode ERROR_EMPTY_CODE
            = new ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            1, "code不能为空");

    public static final ErrorCode ERROR_USER_NOT_FOUND
            = new ErrorCode(HttpServletResponse.SC_NOT_FOUND, MODEL_CODE,
            2, "用户不存在");

    public static final ErrorCode ERROR_USER_DISABLE
            = new ErrorCode(HttpServletResponse.SC_FORBIDDEN, MODEL_CODE,
            3, "用户已被禁用");


    public static final ErrorCode ERROR_INVALID_ID
            = new ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            5, "无效的 id 值");

    public static final ErrorCode ERROR_USER_EXISTED
            = new ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            6, "用户已存在在列表中");

    public static final ErrorCode ERROR_ROLE_NOT_FOUND
            = new ErrorCode(HttpServletResponse.SC_NOT_FOUND, MODEL_CODE,
            7, "角色不存在");

    public static final ErrorCode ERROR_EMPTY_ROLE_NAME
            = new ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            8, "角色名不能为空");

    public static final ErrorCode ERROR_DUPLICATE_ROLE_NAME
            = new ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            9, "角色名已存在");

    public static final ErrorCode ERROR_PERMISSION_NOT_FOUND
            = new ErrorCode(HttpServletResponse.SC_NOT_FOUND, MODEL_CODE,
            10, "权限不存在");

    public static final ErrorCode ERROR_PERMISSION_DENY
            = new ErrorCode(HttpServletResponse.SC_FORBIDDEN, MODEL_CODE,
            11, "权限不足");

    public static final ErrorCode ERROR_INVALID_PARAM
            = new ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            12, "无效的参数。%s");

    public static final ErrorCode ERROR_USERNAME_OR_PASSWD
            = new ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            13, "用户名或密码错误");

    public static final ErrorCode ERROR_PASSWD_TOO_SHORT_OR_TOO_LONG
            = new ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            14, "密码最低不能少于6位，最高不能超过18位");


    public static final ErrorCode ERROR_DELETE_SUPER_OR_ITSELF_ACCOUNT
            = new ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            16, "禁止删除超级管理员账户或自身账户");

    public static final ErrorCode ERROR_ROLE_HAS_BEEN_USED
            = new ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            17, "该角色已经被使用，禁止删除");


    public static final ErrorCode ERROR_UPDATE_MANAGER_ROLE_ITSELF
            = new ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            18, "管理员禁止修改自己的角色");



    /**
     * 超级管理员Id
     */
    public static final long SUPER_MAN_ID = 1;


    /**
     * 权限
     */
    public static class Pm {
        private static final long BASE = MODEL_CODE * 1000;
        public static final long MANAGE_ACCOUNT = BASE + 1;
    }

    public static class Role {
        public static final long SYS = 1;
        public static final long DEVELOPER = 2;
    }
}
