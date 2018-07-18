package com.tmindtech.api.lotteryDraw.model

import com.tmindtech.api.base.model.BaseEntity
import tk.mybatis.mapper.common.Mapper
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "t_contact_info")
open class ContactInfo(var name: String? = "", var phone: String? = "", var uuid: String? = "") : BaseEntity() {


}


class ContactInfoWithPrizeWinnerInfo(var prizewinner: Boolean, contactInfo: ContactInfo):
        ContactInfo(contactInfo.name, contactInfo.phone) {
    init {
        this.id = contactInfo.id;
        this.createTime = contactInfo.createTime;
        this.modifyTime = contactInfo.modifyTime;
    }
}


