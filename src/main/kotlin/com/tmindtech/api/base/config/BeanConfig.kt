package com.tmindtech.api.base.config

import com.tmindtech.api.base.converter.MyProtobufHttpMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportResource


@Configuration
open class BeanConfig {

    //Enable protobuf converter
    @Bean
    open fun protobufHttpMessageConverter(): MyProtobufHttpMessageConverter {
        return MyProtobufHttpMessageConverter()
    }
}