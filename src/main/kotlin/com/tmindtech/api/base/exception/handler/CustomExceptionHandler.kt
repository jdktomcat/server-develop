package com.tmindtech.api.base.exception.handler

import com.google.common.base.Strings
import com.tmindtech.api.base.exception.AwesomeException
import com.tmindtech.api.base.exception.BadRequestException
import com.tmindtech.api.base.exception.ErrorMessage
import com.tmindtech.api.base.exception.ExceptionHelper
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * 全局捕获自定义的异常，返回格式化后的标准输出
 * 异常捕获优先级设定高于[GlobalExceptionHandler]
 *
 * @see AwesomeException
 *
 * @see GlobalExceptionHandler
 *
 *
 * Created by RexQian on 2017/2/9.
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class CustomExceptionHandler {

    @Value("\${debug}")
    private val isDebug: Boolean = false

    @ExceptionHandler(AwesomeException::class)
    @ResponseBody
    fun handleError(req: HttpServletRequest,
                    rsp: HttpServletResponse, ex: AwesomeException): ErrorMessage {
        rsp.status = ex.statusCode
        return toErrorMessage(ex)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class,
            ServletRequestBindingException::class,
            MethodArgumentTypeMismatchException::class,
            HttpRequestMethodNotSupportedException::class)
    @ResponseBody
    fun handleError(req: HttpServletRequest,
                    rsp: HttpServletResponse,
                    ex: Exception): ErrorMessage {
        rsp.status = HttpServletResponse.SC_BAD_REQUEST
        val errorMessage = toErrorMessage(BadRequestException())
        //should not output exception in Production Env
        if (isDebug) {
            errorMessage.debugMessage = ExceptionHelper.getStackTrace(ex)
        }

        return errorMessage
    }

    private fun toErrorMessage(ex: AwesomeException): ErrorMessage {
        val errorMessage = ErrorMessage()
        errorMessage.code = ex.code
        errorMessage.message = Strings.nullToEmpty(ex.message)

        //should not output exception in Production Env
        if (isDebug) {
            errorMessage.debugMessage = ExceptionHelper.getStackTrace(ex)
        }
        return errorMessage
    }
}
