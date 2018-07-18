package com.tmindtech.api.base.exception

/**
 * 自定义异常基础类
 * 从RuntimeException继承以直接支持Transactional
 * Created by RexQian on 2017/2/9.
 */
open class AwesomeException : RuntimeException {

    /**
     * HTTP请求返回码
     */
    var statusCode: Int = 0
    /**
     * 错误码
     */
    var code: Int = 0

    /**
     * @param statusCode HTTP请求返回码
     * @param code 错误码
     * @param message 错误消息
     */
    constructor(statusCode: Int, code: Int, message: String) : super(message) {
        this.statusCode = statusCode
        this.code = code
    }

    constructor(errorCode: ErrorCode) : super(errorCode.message) {
        this.statusCode = errorCode.httpStatus
        this.code = errorCode.code
    }

    override fun toString(): String {
        return "statusCode: " + statusCode + "\ncode: " + code + "\n" + super.toString()
    }
}
