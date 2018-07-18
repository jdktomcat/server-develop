package com.tmindtech.api.example

import com.github.pagehelper.Page
import com.github.pagehelper.PageHelper
import com.tmindtech.api.base.annotation.AwesomeParam
import com.tmindtech.api.example.db.ExampleMapper
import com.tmindtech.api.example.model.Example
import com.tmindtech.api.model.convert.toProto
import com.tmindtech.api.model.protobuf.ExampleProtos
import com.tmindtech.api.model.protobuf.buildExampleList
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Created by RexQian on 2017/7/10.
 */
@RestController
@RequestMapping("/examples")
class ExampleController {
    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var exampleMapper: ExampleMapper

    @GetMapping
    fun getList(@AwesomeParam(required = false) nameLike: String?,
                @AwesomeParam(defaultValue = "0") offset: Int,
                @AwesomeParam(defaultValue = "100") limit: Int
    ): ExampleProtos.ExampleList {
        logger.info { "log test" }

        PageHelper.offsetPage<Any>(offset, limit)
        val list = exampleMapper.getList(nameLike) as Page
        return buildExampleList {
            this.offset = list.startRow
            count = list.count()
            totalCount = list.total.toInt()
            for (item in list) {
                addExamples(item.toProto())
            }
        }
    }

    @PostMapping
    fun add(@RequestBody req: ExampleProtos.AddExample): ExampleProtos.Example {
        val example = Example(req.name, req.description, req.extras)
        exampleMapper.insertSelective(example)
        return exampleMapper.selectByPrimaryKey(example.id).toProto()
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ExampleProtos.Example {
        val result = exampleMapper.selectByPrimaryKey(id) ?: throw Exception()
        return result.toProto()
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        exampleMapper.deleteByPrimaryKey(id)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody req: ExampleProtos.AddExample) {
        val example = Example(req.name, req.description, req.extras)
        example.id = id
        exampleMapper.updateByPrimaryKeySelective(example)
    }
}
