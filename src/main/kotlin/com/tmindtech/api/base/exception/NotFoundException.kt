package com.tmindtech.api.base.exception

import com.tmindtech.api.base.Config

/**
 * Created by RexQian on 2017/8/19.
 */
class NotFoundException(message: String)
    : AwesomeException(Config.ERROR_NOT_FOUND.format(message))
