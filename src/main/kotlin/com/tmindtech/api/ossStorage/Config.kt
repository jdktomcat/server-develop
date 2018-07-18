package com.tmindtech.api.ossStorage

import javax.servlet.http.HttpServletResponse
import com.tmindtech.api.base.exception.ErrorCode



object Config {

    @JvmStatic
    val MODEL_CODE:Int = 3

    @JvmStatic
    val ERROR_VERIFY_FAIL = ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            1, "认证失败")

    @JvmStatic
    val ERROR_FILE_EXISTED = ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            2, "文件已存在")

    @JvmStatic
    val CONTACT_EXISTED = ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            3, "姓名或者手机号已经存在")
    object Pm {
        const val MANAGE_PHOTO =  3001L;
    }

}