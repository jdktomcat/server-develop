package com.tmindtech.api.ossStorage.model

data class CallBackBody(val callbackUrl: String,
                        val callbackBody: String = "filename=\${object}&size=\${size}&mimeType=\${mimeType}&height=\${imageInfo.height}&width=\${imageInfo.width}",
                        val callbackBodyType: String = "application/x-www-form-urlencoded")