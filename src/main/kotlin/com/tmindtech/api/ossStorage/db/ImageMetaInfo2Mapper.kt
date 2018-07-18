package com.tmindtech.api.ossStorage.db

import com.github.pagehelper.Page
import com.tmindtech.api.ossStorage.model.FileRecords
import com.tmindtech.api.ossStorage.model.ImageMetaInfoV2
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import tk.mybatis.mapper.common.Mapper
import java.sql.Timestamp

interface ImageMetaInfo2Mapper: Mapper<ImageMetaInfoV2>  {

    fun getImages(
            @Param("poseQuery") pose: List<Int>?,
            @Param("sceneQuery") scene: List<Int>?,
            @Param("genderQuery") gender: List<Int>?,
            @Param("glassQuery") glass: List<Int>?,
            @Param("beardQuery") beard: List<Int>?,
            @Param("startTime") startTime: Timestamp?,
            @Param("endTime") endTime: Timestamp?,
            @Param("nameLike") nameLike: String? ): Page<FileRecords>;


    fun getImageByIds(@Param("idList") idList: ArrayList<Long>?): Page<FileRecords>;

    @Select("select * from t_image_metainfo2 where file_id = #{id}")
    fun getImageById(@Param("id") fileId: Long?): ImageMetaInfoV2?;


    @Select("select file_id from t_image_metainfo2 where uuid = #{uuid}")
    fun getImageIdsByContactUUID(@Param("uuid") uuid: String?): List<Long>?;

}