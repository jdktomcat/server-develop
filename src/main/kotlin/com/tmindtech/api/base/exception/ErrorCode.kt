package com.tmindtech.api.base.exception

/**
 * 错误码
 * Created by RexQian on 2017/2/16.
 */
class ErrorCode
/**
 *
 * @param httpStatus HTTP状态码 [javax.servlet.http.HttpServletRequest]
 * @param modelCode 模块编号
 * @param errorCode 错误编号
 * @param message 错误消息
 */
(val httpStatus: Int,
 private val modelCode: Int,
 private val errorCode: Int,
 val message: String) {

    /**
     * 格式化error message
     * @param args 格式化参数
     * @return 返回格式化后的新 [ErrorCode]
     */
    fun format(vararg args: Any): ErrorCode {
        return ErrorCode(httpStatus, modelCode, errorCode,
                String.format(this.message, *args))
    }

    /**
     * 错误码定义
     * HTTP状态码（3位） + 模块编号(3位） + 错误编号（3位）
     * 位数不足补0
     * @return 错误码
     */
    val code: Int
        get() = httpStatus * 1000000 + modelCode * 1000 + errorCode
}
