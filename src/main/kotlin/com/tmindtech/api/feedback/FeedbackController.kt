package com.tmindtech.api.feedback

import com.github.pagehelper.Page
import com.github.pagehelper.PageHelper
import com.tmindtech.api.base.annotation.Auth
import com.tmindtech.api.base.annotation.AwesomeParam
import com.tmindtech.api.base.annotation.Permission
import com.tmindtech.api.base.exception.AwesomeException
import com.tmindtech.api.base.model.DataList
import com.tmindtech.api.feedback.model.Feedback
import com.tmindtech.api.feedback.model.db.FeedbackMapper
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/feedbacks")
class FeedbackController {

    @Autowired
    lateinit var feedbackMapper: FeedbackMapper;

    @PostMapping
    //@Auth
    fun addFeedback(@RequestBody feedbackReq: Feedback) {
        if (StringUtils.isEmpty(feedbackReq.content)) {
            throw AwesomeException(Config.CONTENT_NOT_NULL);
        }

        if (StringUtils.isEmpty(feedbackReq.contact)) {
            throw AwesomeException(Config.CONTACT_NOT_NULL);
        }

        if(feedbackReq.content.length > 500) {
            throw  AwesomeException(Config.CONTENT_EXCEED_LIMIT);
        }

        feedbackMapper.insertSelective(feedbackReq);
    }

    @GetMapping()
    @Auth(Permission(Config.Pm.MANAGE_FEEDBACK))
    fun getFeedbacks(@AwesomeParam(defaultValue = "0") offset: Int,
                     @AwesomeParam(name = "limit", defaultValue = "10") limit: Int): DataList<Feedback> {
        PageHelper.offsetPage<Any>(offset, limit);
        val feedbacks: Page<Feedback> = feedbackMapper.selectFeedbacks();
        return DataList(feedbacks);
    }

}