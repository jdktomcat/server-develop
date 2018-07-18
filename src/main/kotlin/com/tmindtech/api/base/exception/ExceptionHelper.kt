package com.tmindtech.api.base.exception

import com.google.gson.Gson
import org.springframework.web.client.RestClientResponseException
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Created by RexQian on 2017/2/12.
 */
object ExceptionHelper {

    fun getStackTrace(ex: Exception): String {
        val writer = StringWriter()
        ex.printStackTrace(PrintWriter(writer))
        val lines = writer.toString().split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val sb = StringBuilder()
        var hitFlag = false
        for (line in lines) {
            if (line.contains("com.tmindtech.api")) {
                hitFlag = true
            } else if (hitFlag) {
                break
            }
            sb.append(line).append("\n")
        }
        return sb.toString()
    }

    fun from(ex: RestClientResponseException): AwesomeException {
        val gson = Gson()
        val errorMessage = gson.fromJson(ex.responseBodyAsString,
                ErrorMessage::class.java)
        return AwesomeException(ex.rawStatusCode,
                errorMessage.code, errorMessage.message)
    }
}
