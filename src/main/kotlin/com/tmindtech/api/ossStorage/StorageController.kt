package com.tmindtech.api.ossStorage

import com.aliyun.oss.OSSClient
import com.aliyun.oss.common.utils.BinaryUtil
import com.aliyun.oss.model.PolicyConditions
import com.google.gson.Gson
import com.tmindtech.api.base.annotation.Auth
import com.tmindtech.api.base.exception.AwesomeException
import com.tmindtech.api.ossStorage.db.FileRecordsMapper
import com.tmindtech.api.ossStorage.model.CallBackBody
import com.tmindtech.api.ossStorage.model.FileRecords
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.client.BufferingClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import java.io.IOException
import java.io.InputStream
import java.net.URLDecoder
import java.nio.charset.Charset
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/oss")
class OssController {
    private val logger = KotlinLogging.logger {}

    @Value("${'$'}{oss.endpoint}")
    lateinit var endpoint: String

    @Value("${'$'}{oss.accessId}")
    lateinit var accessId: String

    @Value("${'$'}{oss.accessKey}")
    lateinit var accessKey: String

    @Value("${'$'}{oss.bucket}")
    lateinit var bucket: String

    @Value("${'$'}{oss.callbackUrl}")
    lateinit var callbackUrl: String

    @Value("${'$'}{oss.expireTime}")
    var expireTime: Long = 0


    @Autowired
    lateinit var fileRecordsMapper: FileRecordsMapper

    lateinit var restTemplate: RestTemplate

    lateinit var ossClient: OSSClient



    @PostConstruct
    fun init() {
        restTemplate = RestTemplate(BufferingClientHttpRequestFactory(SimpleClientHttpRequestFactory()))
        ossClient = OSSClient(endpoint, accessId, accessKey)
    }

    @GetMapping("policy")
    //@Auth
    fun getPolicy(): Map<String, String> {
        val host = "http://${bucket}.${endpoint}"
        val policyConds = PolicyConditions()
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000)
        val expireEndTime = System.currentTimeMillis() + expireTime * 1000
        val expireationDate = Date(expireEndTime)
        val postPolicy: String = ossClient.generatePostPolicy(expireationDate, policyConds)
        val binaryData = postPolicy.toByteArray(Charset.forName("utf-8"))
        val encodedPolicy = BinaryUtil.toBase64String(binaryData)
        val postSignature = ossClient.calculatePostSignature(postPolicy)

        var respMap = mutableMapOf<String, String>()
        respMap.put("accessid", accessId)
        respMap.put("policy", encodedPolicy)
        respMap.put("signature", postSignature)
        respMap.put("host", host)
        respMap.put("dir", "")
        respMap.put("expire", "${expireTime}")
        val callBackBody = CallBackBody(callbackUrl)
        val gson = Gson()
        val callBackStrBase64 = BinaryUtil.toBase64String(
                gson.toJson(callBackBody).toByteArray(Charset.forName("utf-8")))
        respMap.put("callback", callBackStrBase64)
        return respMap
    }

    @PostMapping("callback")
    fun callback(request: HttpServletRequest, @RequestBody ossCallbackBody: String): FileRecords {
        val ret = VerifyOSSCallbackRequest(request, ossCallbackBody)
        val md5 = request.getHeader("Content-MD5")
        println("verify result:" + ret)
        println("OSS Callback Body:" + ossCallbackBody)
        val expireEndTime = System.currentTimeMillis() + expireTime * 1000
        val expireationDate = Date(expireEndTime)
        if (!ret) {
            throw AwesomeException(Config.ERROR_VERIFY_FAIL)
        } else {
            var fileRecords: FileRecords? = fileRecordsMapper.selectByMd5(md5)
            if (fileRecords != null) {
                //val url: String = ossClient.generatePresignedUrl(bucket, fileRecords.name, expireationDate).toString()
                var url: String = "http://${bucket}.${endpoint}/${fileRecords.name}"
                fileRecords.url = url
                return fileRecords
            } else {
                fileRecords = writeFileRecord(ossCallbackBody, md5)
                val url: String = ossClient.generatePresignedUrl(bucket, fileRecords.name, expireationDate).toString()
                fileRecords.url = url
                return fileRecords
            }

        }
    }


    fun getPostBody(`is`: InputStream, contentLen: Int): String {
        if (contentLen > 0) {
            var readLen = 0
            var readLengthThisTime: Int
            val message = ByteArray(contentLen)
            try {
                while (readLen != contentLen) {
                    readLengthThisTime = `is`.read(message, readLen, contentLen - readLen)
                    if (readLengthThisTime == -1) {// Should not happen.
                        break
                    }
                    readLen += readLengthThisTime
                }
                return String(message)
            } catch (e: IOException) {
            }

        }
        return ""
    }

    @Throws(NumberFormatException::class, IOException::class)
    protected fun VerifyOSSCallbackRequest(request: HttpServletRequest, ossCallbackBody: String): Boolean {
        var ret: Boolean
        val autorizationInput = "${request.getHeader("Authorization")}"
        val pubKeyInput = request.getHeader("x-oss-pub-key-url")
        val authorization = BinaryUtil.fromBase64String(autorizationInput)
        val pubKey = BinaryUtil.fromBase64String(pubKeyInput)
        val pubKeyAddr = String(pubKey)
        if (!pubKeyAddr.startsWith("http://gosspublic.alicdn.com/") && !pubKeyAddr.startsWith("https://gosspublic.alicdn.com/")) {
            println("pub key addr must be oss addrss")
            return false
        }
        var retString = executeGet(pubKeyAddr)
        retString = retString!!.replace("-----BEGIN PUBLIC KEY-----", "")
        retString = retString.replace("-----END PUBLIC KEY-----", "")
        retString = retString.replace("\n", "")
        val queryString = request.queryString
        val uri = request.requestURI
        val decodeUri = java.net.URLDecoder.decode(uri, "UTF-8")
        var authStr = decodeUri
        if (queryString != null && queryString != "") {
            authStr += "?" + queryString
        }
        authStr += "\n" + ossCallbackBody
        ret = doCheck(authStr, authorization, retString)
        return ret
    }

    fun doCheck(content: String, sign: ByteArray, publicKey: String): Boolean {
        try {
            val keyFactory = KeyFactory.getInstance("RSA")
            val encodedKey = BinaryUtil.fromBase64String(publicKey)
            val pubKey = keyFactory.generatePublic(X509EncodedKeySpec(encodedKey))
            val signature = java.security.Signature.getInstance("MD5withRSA")
            signature.initVerify(pubKey)
            signature.update(content.toByteArray())
            return signature.verify(sign)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun writeFileRecord(ossCallbackBody: String, md5: String): FileRecords {
        var map: MutableMap<String, String> = mutableMapOf()
        var splitedArr = ossCallbackBody.split("&")
        splitedArr.forEach({ item -> map.put(item.split("=")[0], item.split("=")[1]) })
        var filename = map.get("filename")
        var size = map.get("size")!!.toLong()
        var mimeType = URLDecoder.decode(map.get("mimeType"), "UTF-8")
        var height = map.get("height")!!.toInt()
        var width = map.get("width")!!.toInt()
        val fileRecord: FileRecords = FileRecords(filename, size, mimeType, height, width, md5)
        fileRecordsMapper.insertSelective(fileRecord)
        return fileRecordsMapper.selectByPrimaryKey(fileRecord.id)
    }


    fun executeGet(url: String): String? {
        val res = restTemplate.getForEntity(url, java.lang.String::class.java)
        return res.body.toString()
    }


}