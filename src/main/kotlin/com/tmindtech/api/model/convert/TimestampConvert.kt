package com.tmindtech.api.model.convert

import com.google.protobuf.Timestamp
import java.util.*
import java.sql.Timestamp as SqlTimestamp

fun Date.toProto(): Timestamp {
    val millis = this.time
    return Timestamp.newBuilder()
            .setSeconds(millis / 1000)
            .setNanos((((millis % 1000) * 1000000).toInt()))
            .build()
}

fun Timestamp.toModel(): SqlTimestamp {
    return SqlTimestamp(seconds * 1000 + nanos / 1000000)
}

