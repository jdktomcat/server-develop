package com.tmindtech.api.ossStorage.model

import com.tmindtech.api.base.model.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table

/**
 * @author lwtang
 */
@Entity
@Table(name = "t_file_records")
class FileRecords(
        var name: String? = "",
        var size: Long? = 0,
        var mimeType: String? = "",
        var height: Int? = 0,
        var width: Int? = 0,
        var md5: String? = ""
): BaseEntity() {

    @javax.persistence.Transient
    var url: String? = ""

}

