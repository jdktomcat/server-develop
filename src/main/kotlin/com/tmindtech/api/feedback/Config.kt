package com.tmindtech.api.feedback

import com.tmindtech.api.base.exception.ErrorCode
import javax.servlet.http.HttpServletResponse



object Config {

    @JvmStatic
    val MODEL_CODE:Int = 4

    @JvmStatic
    val CONTACT_NOT_NULL = ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            1, "联系方式不允许为空")

    @JvmStatic
    val CONTENT_NOT_NULL = ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            2, "内容不允许为空")


    @JvmStatic
    val CONTENT_EXCEED_LIMIT = ErrorCode(HttpServletResponse.SC_BAD_REQUEST, MODEL_CODE,
            3, "内容超出字数限制")

    object Pm {

        private val BASE = (MODEL_CODE * 1000).toLong()

        const val  MANAGE_FEEDBACK: Long = 4001;
    }
}