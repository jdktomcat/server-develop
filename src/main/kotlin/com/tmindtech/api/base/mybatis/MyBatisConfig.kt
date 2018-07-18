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

import com.github.pagehelper.PageHelper
import java.util.Properties
import javax.sql.DataSource
import org.apache.ibatis.plugin.Interceptor
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.type.TypeHandler
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.SqlSessionTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.annotation.TransactionManagementConfigurer

/**
 * MyBatis基础配置

 * @author yqpeng
 * *
 * @since 2017-02-10 10:11
 */
@Configuration
@EnableTransactionManagement
open class MyBatisConfig : TransactionManagementConfigurer {

    @Autowired
    lateinit var dataSource: DataSource

    @Bean(name = arrayOf("sqlSessionFactory"))
    open fun sqlSessionFactoryBean(): SqlSessionFactory {
        val bean = SqlSessionFactoryBean()
        bean.setDataSource(dataSource)

        //分页插件
        val pageHelper = PageHelper()
        val properties = Properties()
        properties.setProperty("supportMethodsArguments", "true")
        properties.setProperty("returnPageInfo", "check")
        properties.setProperty("params", "count=countSql")
        pageHelper.setProperties(properties)

        //添加插件
        bean.setPlugins(arrayOf<Interceptor>(pageHelper))

        //添加XML目录
        val resolver = PathMatchingResourcePatternResolver()
        try {
            bean.setMapperLocations(resolver.getResources("classpath:/db/mapper/*.xml"))
            bean.setConfigLocation(resolver.getResource("classpath:/db/mybatis-config.xml"))
            bean.setTypeHandlers(arrayOf(ListTypeHandler()))
            return bean.`object`
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw RuntimeException(ex)
        }
    }

    @Bean
    open fun sqlSessionTemplate(sqlSessionFactory: SqlSessionFactory): SqlSessionTemplate {
        return SqlSessionTemplate(sqlSessionFactory)
    }

    @Bean
    override fun annotationDrivenTransactionManager(): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }
}
