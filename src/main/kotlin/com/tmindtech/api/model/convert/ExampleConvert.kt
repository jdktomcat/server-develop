package com.tmindtech.api.model.convert

import com.tmindtech.api.example.model.Example
import com.tmindtech.api.model.protobuf.ExampleProtos
import com.tmindtech.api.model.protobuf.buildExample

fun Example.toProto(): ExampleProtos.Example {
    val example = this
    return buildExample {
        example.id?.let { this.id = example.id as Long }
        name = example.name
        description = example.description
        extras = example.extras
        createTime = example.createTime?.toProto()
        modifyTime = example.modifyTime?.toProto()
    }
}