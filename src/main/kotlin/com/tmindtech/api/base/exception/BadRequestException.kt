package com.tmindtech.api.base.exception

import com.tmindtech.api.base.Config

/**
 * Created by RexQian on 2017/9/22.
 */
class BadRequestException
    : AwesomeException(Config.ERROR_BAD_PARAM)
