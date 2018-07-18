package com.tmindtech.api.example.model

import com.tmindtech.api.base.model.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table

/**
 * Created by RexQian on 2017/7/10.
 */
@Entity
@Table(name = "t_example")
class Example(
        var name: String? = "",
        var description: String? = null,
        var extras: String? = null
) : BaseEntity()
