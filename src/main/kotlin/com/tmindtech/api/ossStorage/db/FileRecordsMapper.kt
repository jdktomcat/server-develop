package com.tmindtech.api.ossStorage.db

import com.tmindtech.api.ossStorage.model.FileRecords
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import tk.mybatis.mapper.common.Mapper

interface FileRecordsMapper: Mapper<FileRecords> {

    @Select("select * from t_file_records where md5 = #{md5}")
    fun selectByMd5(@Param("md5") md5: String): FileRecords?



    @Update("update t_file_records set contact_name = #{name},contact_uuid = #{uuid} where id = #{fileId}")
    fun updateFileUploaderNameAndUUID(@Param("fileId") fileId: Long,
                                      @Param("name") name: String,
                                      @Param("uuid") uuid: String
                                      );
}