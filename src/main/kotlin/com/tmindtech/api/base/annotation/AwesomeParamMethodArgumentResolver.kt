package com.tmindtech.api.base.annotation

import com.google.common.base.CaseFormat
import org.springframework.core.MethodParameter
import org.springframework.web.bind.annotation.ValueConstants
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.support.MultipartResolutionDelegate
import org.springframework.web.util.WebUtils
import javax.servlet.http.HttpServletRequest

/**
 * Created by RexQian on 2017/2/14.
 */
class AwesomeParamMethodArgumentResolver : AbstractNamedValueMethodArgumentResolver() {
    private var isEnableLowerUnderscoreName = true

    fun setLowerUnderscoreName(isEnable: Boolean) {
        this.isEnableLowerUnderscoreName = isEnable
    }

    override fun createNamedValueInfo(parameter: MethodParameter): AbstractNamedValueMethodArgumentResolver.NamedValueInfo {
        val ann = parameter.getParameterAnnotation(AwesomeParam::class.java)
        return if (ann != null) AwesomeParamNamedValueInfo(ann) else AwesomeParamNamedValueInfo()
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(AwesomeParam::class.java)
    }

    @Throws(Exception::class)
    override fun resolveName(name: String, parameter: MethodParameter, request: NativeWebRequest): Any? {
        var value = name
        if (isEnableLowerUnderscoreName) {
            value = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)
        }
        return internalResolveName(value, parameter, request)
    }

    /**
     * @see RequestParamMethodArgumentResolver
     */
    @Throws(Exception::class)
    private fun internalResolveName(name: String, parameter: MethodParameter,
                                    request: NativeWebRequest): Any? {

        val servletRequest = request.getNativeRequest(HttpServletRequest::class.java)
        val multipartRequest = WebUtils.getNativeRequest(servletRequest, MultipartHttpServletRequest::class.java)

        val mpArg = MultipartResolutionDelegate.resolveMultipartArgument(name, parameter, servletRequest)
        if (mpArg !== MultipartResolutionDelegate.UNRESOLVABLE) {
            return mpArg
        }

        var arg: Any? = null
        if (multipartRequest != null) {
            val files = multipartRequest.getFiles(name)
            if (!files.isEmpty()) {
                arg = if (files.size == 1) files[0] else files
            }
        }
        if (arg == null) {
            val paramValues = request.getParameterValues(name)
            if (paramValues != null) {
                arg = if (paramValues.size == 1) paramValues[0] else paramValues
            }
        }
        return arg
    }

    private class AwesomeParamNamedValueInfo : AbstractNamedValueMethodArgumentResolver.NamedValueInfo {

        constructor() : super("", false, ValueConstants.DEFAULT_NONE) {}

        constructor(annotation: AwesomeParam) : super(annotation.name, annotation.required, annotation.defaultValue) {}
    }

}
