package com.tmindtech.api.model.protobuf

import com.tmindtech.api.model.protobuf.*
import com.tmindtech.api.model.protobuf.ExampleProtos
import com.tmindtech.api.model.protobuf.ExampleProtos.ExampleOrBuilder
import com.tmindtech.api.model.protobuf.ExampleProtos.Example
import com.tmindtech.api.model.protobuf.ExampleProtos.Example.Builder
import com.tmindtech.api.model.protobuf.ExampleProtos.ExampleListOrBuilder
import com.tmindtech.api.model.protobuf.ExampleProtos.ExampleList
import com.tmindtech.api.model.protobuf.ExampleProtos.AddExampleOrBuilder
import com.tmindtech.api.model.protobuf.ExampleProtos.AddExample

public inline fun buildExample(fn: Example.Builder.() -> Unit): Example {
    val builder = Example.newBuilder()
    builder.fn()
    return builder.build()
}
public inline fun buildExampleList(fn: ExampleList.Builder.() -> Unit): ExampleList {
    val builder = ExampleList.newBuilder()
    builder.fn()
    return builder.build()
}
public inline fun buildAddExample(fn: AddExample.Builder.() -> Unit): AddExample {
    val builder = AddExample.newBuilder()
    builder.fn()
    return builder.build()
}
