package com.tmindtech.api.ossStorage.model

import com.tmindtech.api.base.model.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "t_image_metainfo2")
@Entity
class ImageMetaInfoV2(
        val fileId: Int = 0,
        val pose: Int = 0, val scene: Int = 0, val gender: Int = 0, val age: Int = 0,
        val glass: Int = 0, val beard: Int = 0, val uuid: String = "") : BaseEntity() {

}


