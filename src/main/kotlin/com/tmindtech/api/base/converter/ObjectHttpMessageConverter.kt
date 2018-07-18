package com.tmindtech.api.base.converter

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.protobuf.ExtensionRegistry
import com.google.protobuf.Message
import com.googlecode.protobuf.format.JsonFormat
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.util.MultiValueMap
import org.springframework.util.StreamUtils
import org.springframework.util.StringUtils
import java.io.ByteArrayInputStream
import java.io.IOException
import java.lang.reflect.Method
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

/**
 *
 * Converts HTTP requests with bodies that are application/x-www-form-urlencoded or multipart/form-data to an Object
 * annotated with [org.springframework.web.bind.annotation.RequestBody] in the the handler method.

 * @author Jesse Swidler
 */
class ObjectHttpMessageConverter : HttpMessageConverter<Any> {

    private val formHttpMessageConverter = FormHttpMessageConverter()

    private val charset = StandardCharsets.UTF_8

    private val extensionRegistry = ExtensionRegistry.newInstance()

    override fun canRead(clazz: Class<*>, mediaType: MediaType): Boolean {
        return formHttpMessageConverter.canRead(MultiValueMap::class.java, mediaType)
    }

    override fun canWrite(clazz: Class<*>?, mediaType: MediaType?): Boolean {
        return false
    }

    override fun getSupportedMediaTypes(): List<MediaType> {
        return formHttpMessageConverter.supportedMediaTypes
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class, HttpMessageNotReadableException::class)
    override fun read(clazz: Class<*>, inputMessage: HttpInputMessage): Any {
        val contentType = inputMessage.headers.contentType
        val charset = if (contentType.charset != null) contentType.charset else this.charset
        val body = StreamUtils.copyToString(inputMessage.body, charset)

        val pairs = StringUtils.tokenizeToStringArray(body, "&")
        val objectMap = mutableMapOf<String, Any>()
        for (pair in pairs) {
            val idx = pair.indexOf('=')
            if (idx == -1) {
                continue
            }
            val name = URLDecoder.decode(pair.substring(0, idx), charset.name())
            val value = URLDecoder.decode(pair.substring(idx + 1), charset.name())
            val existValue = objectMap[name]
            if (existValue == null) {
                objectMap.put(name, value)
            } else if (existValue is MutableList<*>) {
                (existValue as MutableList<String>).add(value)
            } else {
                val list = mutableListOf<String>()
                list.add(existValue as String)
                list.add(value)
                objectMap.put(name, list)
            }
        }
        val json = Gson().toJson(objectMap)
        val `is` = ByteArrayInputStream(json.toByteArray())
        try {
            val builder = getMessageBuilder(clazz)
            JSON_FORMAT.merge(`is`, charset, extensionRegistry, builder)
            return builder.build()
        }catch (ex: NoSuchMethodException) {
            return  GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
                    .fromJson(json, clazz)
        } catch (ex: Exception) {
            throw HttpMessageNotReadableException("Could not read FormData message: " + ex.message, ex)
        }

    }

    @Throws(UnsupportedOperationException::class)
    override fun write(obj: Any, contentType: MediaType, outputMessage: HttpOutputMessage) {
        throw UnsupportedOperationException("")
    }

    companion object {

        private val methodCache = ConcurrentHashMap<Class<*>, Method>()

        private val JSON_FORMAT = MyJsonFormat()

        /**
         * Create a new `Message.Builder` instance for the given class.
         *
         * This method uses a ConcurrentHashMap for caching method lookups.
         */
        @Throws(Exception::class)
        private fun getMessageBuilder(clazz: Class<*>): Message.Builder {
            var method: Method? = methodCache[clazz]
            if (method == null) {
                method = clazz.getMethod("newBuilder")
                methodCache.put(clazz, method!!)
            }
            return method.invoke(clazz) as Message.Builder
        }
    }
}
