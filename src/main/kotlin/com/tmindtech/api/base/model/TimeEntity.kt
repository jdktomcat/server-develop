package com.tmindtech.api.base.model

import java.sql.Timestamp
import java.time.Instant
import java.util.*
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
open class TimeEntity {

    var createTime: Timestamp? = null

    var modifyTime: Timestamp? = null

    fun preUpdate() {
        modifyTime = Timestamp.from(Instant.now())
    }
}