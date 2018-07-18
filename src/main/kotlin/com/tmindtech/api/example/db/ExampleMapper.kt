package com.tmindtech.api.example.db

import com.tmindtech.api.example.model.Example
import org.apache.ibatis.annotations.Param
import tk.mybatis.mapper.common.Mapper

interface ExampleMapper : Mapper<Example> {

    fun getList(@Param("name_like") nameLike: String?): List<Example>
}