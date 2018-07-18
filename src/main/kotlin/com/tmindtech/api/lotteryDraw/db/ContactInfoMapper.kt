package com.tmindtech.api.lotteryDraw.db

import com.github.pagehelper.Page
import com.tmindtech.api.lotteryDraw.model.ContactInfo
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import tk.mybatis.mapper.common.Mapper

interface ContactInfoMapper: Mapper<ContactInfo> {

    fun getAll(@Param("query") query: String?): Page<ContactInfo>;



    @Select("select name from t_contact_info where uuid = #{uuid} limit 0,1")
    fun getContactNameByUUID(@Param("uuid") uuid: String?): String;

}


