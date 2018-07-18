package com.tmindtech.api.ossStorage.model

import java.util.ArrayList

class ImageMetaInfoV2ReqItem(
        val id: Int = 0,
        val pose: Int = 0, val scene: Int = 0, val gender: Int = 0, val age: Int = 0,
        val glass: Int = 0, val beard: Int = 0)


class ImageMetaInfoV2Req(var metainfo2Req: List<ImageMetaInfoV2ReqItem> = ArrayList())