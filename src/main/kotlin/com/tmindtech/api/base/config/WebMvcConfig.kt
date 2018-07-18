package com.tmindtech.api.base.config

import com.tmindtech.api.base.annotation.AwesomeParamMethodArgumentResolver
import com.tmindtech.api.base.converter.ObjectHttpMessageConverter
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
open class WebMvcConfig : WebMvcConfigurerAdapter() {

    override fun addCorsMappings(registry: CorsRegistry?) {
        super.addCorsMappings(registry)

        registry!!.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*")
    }

    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>?) {
        converters!!.add(ObjectHttpMessageConverter())
    }

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>?) {
        super.addArgumentResolvers(argumentResolvers)
        argumentResolvers!!.add(AwesomeParamMethodArgumentResolver())
    }
}
