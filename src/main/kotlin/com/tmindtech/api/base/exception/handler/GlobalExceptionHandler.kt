package com.tmindtech.api.base.exception.handler

import com.tmindtech.api.base.exception.ErrorMessage
import com.tmindtech.api.base.exception.ExceptionHelper
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * 全局默认异常捕获
 * 捕获优先级最低
 *
 *
 * Created by RexQian on 2017/2/9.
 */
@ControllerAdvice
@Order
class GlobalExceptionHandler {

    @Value("\${debug}")
    private val isDebug: Boolean = false

    @ExceptionHandler(Exception::class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleError(req: HttpServletRequest, ex: Exception): ErrorMessage {
        val errorMessage = ErrorMessage()
        errorMessage.code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        errorMessage.message = "服务器错误，请联系管理员 >.<"

        //should not output exception in Production Env
        if (isDebug) {
            errorMessage.debugMessage = ExceptionHelper.getStackTrace(ex)
        }
        return errorMessage
    }

}
