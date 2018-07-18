package com.tmindtech.api.base

import com.tmindtech.api.base.exception.ErrorCode
import javax.servlet.http.HttpServletResponse

/**
 * Created by RexQian on 2017/2/21.
 */
object Config {
    /**
     * 模块编号
     */
    @JvmField
    val MODEL_CODE = 1

    @JvmField
    val ERROR_NO_AUTH = ErrorCode(HttpServletResponse.SC_UNAUTHORIZED, MODEL_CODE,
            1, "未登录")
    @JvmField
    val ERROR_ACCESS_DENY = ErrorCode(HttpServletResponse.SC_FORBIDDEN, MODEL_CODE,
            2, "未授权")
    @JvmField
    val ERROR_INTERNAL = ErrorCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, MODEL_CODE,
            3, "未预料的错误")
    @JvmField
    val ERROR_BAD_PARAM = ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            4, "非法的请求参数")
    @JvmField
    val ERROR_DEBUG_ONLY = ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            5, "仅调试模式可以")
    @JvmField
    val ERROR_NOT_FOUND = ErrorCode(HttpServletResponse.SC_NOT_FOUND, MODEL_CODE,
            6, "%s不存在")
    @JvmField
    val SUPER_MAN_ID = 1
}
