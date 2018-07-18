package com.tmindtech.api.feedback.model.db

import com.github.pagehelper.Page
import com.tmindtech.api.feedback.model.Feedback
import org.apache.ibatis.annotations.Select
import tk.mybatis.mapper.common.Mapper

interface FeedbackMapper: Mapper<Feedback> {

    @Select("select * from t_feedback order by create_time desc")
    fun selectFeedbacks() : Page<Feedback>;

}