package com.tmindtech.api.feedback.model

import java.sql.Timestamp
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name="t_feedback")
class Feedback(val contact: String, val content: String, val createTime: Timestamp) {
    constructor():this("","", Timestamp.from(Instant.now()))
};