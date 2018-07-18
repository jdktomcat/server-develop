package com.tmindtech.api.ossStorage

import com.aliyun.oss.OSSClient
import com.github.pagehelper.Page
import com.github.pagehelper.PageHelper
import com.tmindtech.api.base.annotation.Auth
import com.tmindtech.api.base.annotation.AwesomeParam
import com.tmindtech.api.base.annotation.Permission
import com.tmindtech.api.base.converter.AwesomeDateTime
import com.tmindtech.api.base.model.DataList
import com.tmindtech.api.lotteryDraw.db.ContactInfoMapper
import com.tmindtech.api.model.UUIDRes
import com.tmindtech.api.ossStorage.db.ImageMetaInfo2Mapper
import com.tmindtech.api.ossStorage.db.ImageMetaInfoMapper
import com.tmindtech.api.ossStorage.model.*
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.math.RandomUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.client.BufferingClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.annotation.PostConstruct
import kotlin.collections.ArrayList


@RestController
@RequestMapping("/image")
class ImageMetaInfoController {

    @Autowired
    lateinit var imageMetaInfoMapper: ImageMetaInfoMapper

    @Autowired
    lateinit var imageMetaInfo2Mapper: ImageMetaInfo2Mapper

    @Autowired
    lateinit var contactInfoMapper: ContactInfoMapper;

    @Value("${'$'}{oss.endpoint}")
    lateinit var endpoint: String

    @Value("${'$'}{oss.accessId}")
    lateinit var accessId: String

    @Value("${'$'}{oss.accessKey}")
    lateinit var accessKey: String

    @Value("${'$'}{oss.bucket}")
    lateinit var bucket: String

    @Value("${'$'}{oss.expireTime}")
    var expireTime: Long = 0

    @Value("\${oss.mount_point}")
    lateinit var ossMountPoint: String;

    @Value("\${ziped_file_target_location}")
    lateinit var zipFileTargetLocation: String;

    @Value("\${ziped_file_link_prefix}")
    lateinit var zipedFileUrlPrefix: String;

    var logger: Logger = LoggerFactory.getLogger(ImageMetaInfoController::class.java);



    lateinit var restTemplate: RestTemplate

    lateinit var ossClient: OSSClient

    @PostConstruct
    fun init() {
        restTemplate = RestTemplate(BufferingClientHttpRequestFactory(SimpleClientHttpRequestFactory()))
        ossClient = OSSClient(endpoint, accessId, accessKey)
    }

    @PostMapping("metainfo")
    fun uploadMetaInfo(@RequestBody requestMetaInfo: String) {
        imageMetaInfoMapper.insertSelective(ImageMetaInfo(requestMetaInfo))
    }

    @PostMapping("metainfo2")
    fun uploadMetaInfoV2(@RequestBody requestMetaInfo2Req: ImageMetaInfoV2Req): UUIDRes {
        var uuidRes = UUIDRes.newInstance();
        requestMetaInfo2Req.metainfo2Req.forEach { item ->
            var imageMetaInfoV2ToInsert = ImageMetaInfoV2(item.id, item.pose, item.scene, item.gender, item.age,
                    item.glass, item.beard, uuidRes.uuid);
            imageMetaInfo2Mapper.insertSelective(imageMetaInfoV2ToInsert);
        };
        return uuidRes;
    }

    //如果有id 传过来, 就按 id 获取, 如果没有就获取全部筛选条件的图片
    @PostMapping("compressed")
    @Auth(Permission(Config.Pm.MANAGE_PHOTO))
    @Throws(IOException::class)
    fun downloadImage(
            @AwesomeParam(required = false) pose: List<Int>?,
            @AwesomeParam(required = false) scene: List<Int>?,
            @AwesomeParam(required = false) gender: List<Int>?,
            @AwesomeParam(required = false) glass: List<Int>?,
            @AwesomeParam(required = false) beard: List<Int>?,
            @AwesomeParam(required = false) startTime: Timestamp?,
            @AwesomeParam(required = false) endTime: Timestamp?,
            @RequestBody filteredImagesReq: FilterImageIdsReq?
    ): ImageLinksRes  {
        var imageLinksRes = ImageLinksRes(ArrayList())
        var imageNames: Page<FileRecords>?;
        if(filteredImagesReq == null || filteredImagesReq.filteredImages == null ||
                filteredImagesReq.filteredImages.size > 0) {
            imageNames = imageMetaInfo2Mapper.getImageByIds(filteredImagesReq?.filteredImages)
        } else {
            imageNames = imageMetaInfo2Mapper.getImages(pose, scene, gender, glass, beard, startTime, endTime, null);
        }


        var imageLocations = ArrayList<String>()
        var imageDescriptions = ArrayList<String>()
        imageNames.forEach({
            imageLocations.add(ossMountPoint + it.name)
            imageDescriptions.add(getFileDescription(it.name, it.id) ?: "")
        });
        //每一千个文件一个zip包, 包名体现筛选规则
        var packageIndex = 0;
        var imageLocationIndex = 0;
        var tempList = ArrayList<String>();
        var tempDescriptionList = ArrayList<String>();
        imageLocations.forEach({
            tempList.add(it);
            tempDescriptionList.add(imageDescriptions.get(imageLocationIndex));
            imageLocationIndex++;

            if (imageLocationIndex / 1000 != packageIndex) {
                var imageNameParamList = StringUtils.join(tempList, " ");
                logger.info("packageIndex:{}, size:{}, image locations: {}",packageIndex, tempList.size, tempList);
                //zip & put file into target dir
                var randomStr: String = RandomUtils.nextLong().toString();
                var targetZipName = generateCompressedFileName(
                        randomStr, pose, scene, gender, glass, beard, packageIndex
                );
                var metaTextFilePath: String = ossMountPoint +  randomStr + "_" + packageIndex + "_meta.txt";
                var fileWriter = FileWriter(metaTextFilePath);
                for ( i in 0..999) {
                    writeToMetaTextFile(fileWriter, tempDescriptionList.get(i))
                }
                fileWriter.close();

                var targetZipLocationName = zipFileTargetLocation + targetZipName;
                var command: String = "zip -r " + targetZipLocationName + " " + imageNameParamList + " " + metaTextFilePath;
                logger.info("zip command: {}", command);
                try {
                    var ps: Process = Runtime.getRuntime().exec(command);
                    ps.waitFor();
                } catch (e: IOException) {
                    logger.error(e.localizedMessage);
                    throw e
                }
                imageLinksRes.links.add(zipedFileUrlPrefix + targetZipName);
                tempList.clear();
                tempDescriptionList.clear();
                File(metaTextFilePath).delete();
                packageIndex++;
            }
        });
        //处理最后剩余的
        if( tempList.size > 0) {
            var imageNameParamList = StringUtils.join(tempList, " ");
            logger.info("packageIndex:{}, size:{}, image locations: {}",packageIndex, tempList.size, tempList);
            //zip & put file into target dir
            var targetZipName = generateCompressedFileName(
                    RandomUtils.nextLong().toString(),
                    pose, scene, gender, glass, beard, packageIndex
            )
            var targetZipLocationName = zipFileTargetLocation + targetZipName;

            var metaTextFilePath: String = ossMountPoint + targetZipName + "_meta.txt";
            var fileWriter = FileWriter(metaTextFilePath);
            for ( i in 0..(tempList.size-1)) {
                writeToMetaTextFile(fileWriter, tempDescriptionList.get(i))
            }
            fileWriter.close();


            var command: String =
                    "/usr/bin/zip -r " + targetZipLocationName + " " + imageNameParamList + " " + metaTextFilePath;
            logger.info("zip command: {}", command);
            var ps: Process = Runtime.getRuntime().exec(command);
            ps.waitFor();
            imageLinksRes.links.add(zipedFileUrlPrefix + targetZipName);
            tempDescriptionList.clear();
            tempList.clear();
            File(metaTextFilePath).delete();
            packageIndex++;
        }

       return imageLinksRes;
    }



    @GetMapping("list")
    @Auth(Permission(Config.Pm.MANAGE_PHOTO))
    fun searchImageList(
            @AwesomeParam(required = false) pose: List<Int>?,
            @AwesomeParam(required = false) scene: List<Int>?,
            @AwesomeParam(required = false) gender: List<Int>?,
            @AwesomeParam(required = false) glass: List<Int>?,
            @AwesomeParam(required = false) beard: List<Int>?,
            @AwesomeParam(required = false) startTime: AwesomeDateTime?,
            @AwesomeParam(required = false) endTime: AwesomeDateTime?,
            @AwesomeParam(required = false) nameLike: String?,
            @AwesomeParam(required = false, defaultValue = "0") offset: Int,
            @AwesomeParam(required = false, defaultValue = "10") limit: Int
                        ): DataList<ImageListItem> {
        PageHelper.offsetPage<Any>(offset, limit);
        var fileReords: Page<FileRecords>  =
                imageMetaInfo2Mapper.getImages(pose, scene, gender, glass, beard, startTime?.timestamp,
                        endTime?.timestamp, nameLike);
        var ImageItemList = ArrayList<ImageListItem>();
        fileReords.forEach({
            var url: String = "http://${bucket}.${endpoint}/${it.name}"
            var username = "";
            var metaInfo = imageMetaInfo2Mapper.getImageById(it.id);
            if( metaInfo != null && metaInfo.uuid != null) {
                username = contactInfoMapper.getContactNameByUUID(metaInfo.uuid);
            }

            ImageItemList.add(ImageListItem(it.id, it.name, url, it.createTime, username));
        })
        var dataList = DataList<ImageListItem>(fileReords.startRow, fileReords.size, fileReords.total, ImageItemList);
        return dataList;
    }


    /**
     * 生成一个压缩包的包名 20180312_011222_(室内、正面、男、无胡子)_0.zip, 20180312_011222_1_(室内、正面、男、无胡子)_1.zip
     *
     */
    @Auth(Permission(Config.Pm.MANAGE_PHOTO))
    fun generateCompressedFileName(randomStr: String,
                                   pose: List<Int>?, scene: List<Int>?, gender: List<Int>?, glass: List<Int>?,
                                   beard: List<Int>?, index: Int?
    ): String {
        var simpleDateFormat = SimpleDateFormat("yyyy_MM_dd");
        var targetZipNameAppender = StringBuffer();
        targetZipNameAppender.append("${simpleDateFormat.format(Date())}_${randomStr}");

        if (index != null) {
            targetZipNameAppender.append("_${index}");
        }

        targetZipNameAppender.append(".zip");

        return targetZipNameAppender.toString();
    }

    /**
     * 获取图片的描述信息
     */
    fun getFileDescription(fileName: String?, fileId: Long?): String? {
        if( fileId == null) {
            return null;
        }
        var imageMetaInfoV2 = imageMetaInfo2Mapper.getImageById(fileId);
        if(imageMetaInfoV2 != null) {
            var descriptionEntry: StringBuilder = StringBuilder();
            if (imageMetaInfoV2.pose != null) {
                when {
                    imageMetaInfoV2.pose.equals(1) -> descriptionEntry.append("常见拍照姿势,")
                    imageMetaInfoV2.pose.equals(2) -> descriptionEntry.append("正面,")
                    imageMetaInfoV2.pose.equals(3) -> descriptionEntry.append("侧面,")
                    imageMetaInfoV2.pose.equals(4) -> descriptionEntry.append("手势拍照,")
                    else -> {
                    }
                }
            }

            if (imageMetaInfoV2.scene != null) {
                when {
                    imageMetaInfoV2.scene.equals(1) -> descriptionEntry.append("室内,")
                    imageMetaInfoV2.scene.equals(2)-> descriptionEntry.append("室外,")
                    imageMetaInfoV2.scene.equals(3) -> descriptionEntry.append("夜景,")
                    else -> {
                    }
                }
            }

            if (imageMetaInfoV2.gender != null) {
                when {
                    imageMetaInfoV2.gender.equals(1) -> descriptionEntry.append("男,")
                    imageMetaInfoV2.gender.equals(2) -> descriptionEntry.append("女,")
                    else -> {
                    }
                }
            }

            if(imageMetaInfoV2.glass != null) {
                when {
                    imageMetaInfoV2.glass.equals(0) -> descriptionEntry.append("无眼镜,")
                    imageMetaInfoV2.glass.equals(1) -> descriptionEntry.append("平光镜,")
                    imageMetaInfoV2.glass.equals(2) -> descriptionEntry.append("太阳镜,")
                    else -> {
                    }
                }
            }

            if(imageMetaInfoV2.beard != null) {
                when {
                    imageMetaInfoV2.beard.equals(0) -> descriptionEntry.append("无胡子")
                    imageMetaInfoV2.beard.equals(1) -> descriptionEntry.append("有胡子")
                    else -> {
                    }
                }
            }
            return fileName + "   " + descriptionEntry.toString();
        }
        return null;
    }


    fun writeToMetaTextFile(fileWrite: FileWriter, content: String): FileWriter {
        fileWrite.write(content);
        fileWrite.write("\n");
        return fileWrite;
    }

}