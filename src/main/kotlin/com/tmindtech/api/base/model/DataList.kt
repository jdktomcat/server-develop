package com.tmindtech.api.base.model

import com.github.pagehelper.Page

class DataList<T>(var offset:Int, var count: Int, var totalCount: Long, var dataList: List<T>) {

    constructor(page: Page<T>):this(page.startRow, page.result.size, page.total, page.result) {

    }
}