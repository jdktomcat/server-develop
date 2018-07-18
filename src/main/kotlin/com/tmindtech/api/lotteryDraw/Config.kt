package com.tmindtech.api.lotteryDraw

import com.tmindtech.api.base.exception.ErrorCode
import com.tmindtech.api.ossStorage.Config
import javax.servlet.http.HttpServletResponse



object Config {

    @JvmStatic
    val MODEL_CODE:Int = 5


    @JvmStatic
    val CONTACT_EMPTY_NOT_ALLOWED = ErrorCode(HttpServletResponse.SC_BAD_REQUEST, Config.MODEL_CODE,
            1, "姓名或者手机号不允许为空")

    object Pm {

        const val  MANAGE_LOTTERY: Long = 5001;
    }
}