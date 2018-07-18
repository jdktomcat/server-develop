package com.tmindtech.api.base.mybatis

import com.tmindtech.api.base.model.TimeEntity
import org.apache.ibatis.executor.Executor
import org.apache.ibatis.mapping.MappedStatement
import org.apache.ibatis.mapping.SqlCommandType
import org.apache.ibatis.plugin.*
import java.util.*

@Intercepts(Signature(type = Executor::class, method = "update",
        args = arrayOf(MappedStatement::class, Any::class)))
class PreUpdate : Interceptor {
    @Throws(Throwable::class)
    override fun intercept(invocation: Invocation): Any {
        val statement = invocation.args[0] as MappedStatement
        if (statement.sqlCommandType != SqlCommandType.UPDATE) {
            return invocation.proceed()
        }

        val entity = invocation.args[1]
        if (entity is TimeEntity) {
            entity.preUpdate()
        }
        return invocation.proceed()
    }

    override fun plugin(target: Any): Any {
        return Plugin.wrap(target, this)
    }

    override fun setProperties(properties: Properties) {

    }
}
