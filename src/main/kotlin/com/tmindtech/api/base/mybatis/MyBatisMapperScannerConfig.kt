/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.tmindtech.api.base.mybatis

import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tk.mybatis.spring.mapper.MapperScannerConfigurer

/**
 * MyBatis扫描接口，使用的tk.mybatis.spring.db.mapper.MapperScannerConfigurer，如果你不使用通用Mapper

 * @author yqpeng
 * *
 * @since 2017-02-10 14:46
 */
@Configuration
@AutoConfigureAfter(MyBatisConfig::class)
open class MyBatisMapperScannerConfig {

    @Bean
    open fun mapperScannerConfigurer(): MapperScannerConfigurer {
        val mapperScannerConfigurer = MapperScannerConfigurer()
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory")
        mapperScannerConfigurer.setBasePackage("com.tmindtech.api.**.db")
        return mapperScannerConfigurer
    }
}
