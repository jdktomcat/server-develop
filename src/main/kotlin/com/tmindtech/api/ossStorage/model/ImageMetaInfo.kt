package com.tmindtech.api.ossStorage.model

import com.tmindtech.api.base.model.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "t_image_metainfo")
@Entity
class ImageMetaInfo(val metaInfo: String) : BaseEntity()