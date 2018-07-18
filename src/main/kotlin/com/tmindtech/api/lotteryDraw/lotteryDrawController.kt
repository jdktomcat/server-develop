package com.tmindtech.api.lotteryDraw

import com.github.pagehelper.Page
import com.github.pagehelper.PageHelper
import com.tmindtech.api.base.annotation.Auth
import com.tmindtech.api.base.annotation.AwesomeParam
import com.tmindtech.api.base.annotation.Permission
import com.tmindtech.api.base.exception.AwesomeException
import com.tmindtech.api.base.model.DataList
import com.tmindtech.api.lotteryDraw.db.ContactInfoMapper
import com.tmindtech.api.lotteryDraw.model.AddPrizeWinnerReq
import com.tmindtech.api.lotteryDraw.model.ContactInfo
import com.tmindtech.api.lotteryDraw.model.ContactInfoWithPrizeWinnerInfo
import com.tmindtech.api.lotteryDraw.model.PrizeWinnerListRes
import com.tmindtech.api.lotteryDraw.service.PrizeWinnerListService
import com.tmindtech.api.ossStorage.db.FileRecordsMapper
import com.tmindtech.api.ossStorage.db.ImageMetaInfo2Mapper
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.ArrayList

@RestController
@RequestMapping("/lottery_draw")
class lotteryDrawController {

    @Autowired
    lateinit var contactInfoMapper: ContactInfoMapper;

    @Autowired
    lateinit var imageMetaInfo2Mapper: ImageMetaInfo2Mapper;

    @Autowired
    lateinit var fileRecordMapper: FileRecordsMapper;

    @Autowired
    lateinit var prizeWinnerListService: PrizeWinnerListService;


    @PostMapping("contacts")
    fun addContact(@RequestBody addContactReq: AddContactReq) {

        if(StringUtils.isEmpty(addContactReq.name) or StringUtils.isEmpty(addContactReq.phone)) {
            throw AwesomeException(Config.CONTACT_EMPTY_NOT_ALLOWED)
        }


        //去掉检重逻辑
        /*var contactInfoToSelect = ContactInfo(null, addContactReq.phone);
        if(contactInfoMapper.select(contactInfoToSelect).size > 0) {
            throw AwesomeException(Config.CONTACT_EXISTED);
        }

        contactInfoToSelect = ContactInfo(addContactReq.name, null);
        if(contactInfoMapper.select(contactInfoToSelect).size > 0) {
            throw AwesomeException(Config.CONTACT_EXISTED);
        }*/

        contactInfoMapper.insertSelective(ContactInfo(addContactReq.name, addContactReq.phone, addContactReq.uuid));
        var ids: List<Long>? = imageMetaInfo2Mapper.getImageIdsByContactUUID(addContactReq.uuid);

        ids?.forEach({
            fileRecordMapper.updateFileUploaderNameAndUUID(it, addContactReq.name, addContactReq.uuid);
        });


    }


    @PostMapping("prizewinners")
    @Auth(Permission(Config.Pm.MANAGE_LOTTERY))
    fun addPrizeWinner(@RequestBody addPrizeWinnerReq: AddPrizeWinnerReq) {
        prizeWinnerListService.addPrizeWinner(addPrizeWinnerReq)
    }

    @DeleteMapping("prizewinners")
    @Auth(Permission(Config.Pm.MANAGE_LOTTERY))
    fun delPrizeWinner(@RequestBody addPrizeWinnerReq: AddPrizeWinnerReq) {
        prizeWinnerListService.delPrizwWinner(addPrizeWinnerReq)
    }

    @GetMapping("prizewinners")
    fun getPrizeWinners(@AwesomeParam published: Boolean,
                        @AwesomeParam(required = false, defaultValue = "0") offset: Int,
                        @AwesomeParam(required = false, defaultValue = "100") limit: Int): PrizeWinnerListRes {

        return prizeWinnerListService.getPrizeWinnerList(published, offset, limit);
    }



    @PostMapping("prizewinners_lists/{id}/publish")
    @Auth(Permission(Config.Pm.MANAGE_LOTTERY))
    fun publishPrizeWinnerList(@PathVariable("id") id: Long) {
        prizeWinnerListService.publishPrizeWinnerList(id);
    }

    @GetMapping("participants")
    //@Auth(Permission(Config.Pm.MANAGE_LOTTERY))
    fun getAllParticipants(@AwesomeParam(required = false) query: String?,
                           @AwesomeParam(required = false, defaultValue = "0") offset: Int,
                           @AwesomeParam(required = false, defaultValue = "100") limit: Int): DataList<ContactInfoWithPrizeWinnerInfo> {
        PageHelper.offsetPage<Any>(offset, limit);
        var contactInfoListWithPrizeWinnerInfo: MutableList<ContactInfoWithPrizeWinnerInfo> = ArrayList();
        var contactPage: Page<ContactInfo> = contactInfoMapper.getAll(query);
        contactPage.result.forEach({
            var isInList = prizeWinnerListService.isContactInTheCurrentPrizeWinnerList(it.id)
            contactInfoListWithPrizeWinnerInfo.add(ContactInfoWithPrizeWinnerInfo(isInList, it));
        });
        var contactInfoWithPrizeWinner = DataList<ContactInfoWithPrizeWinnerInfo>(
                contactPage.startRow, contactPage.result.size, contactPage.total, contactInfoListWithPrizeWinnerInfo);
        return contactInfoWithPrizeWinner;
    }

}

class AddContactReq(var name: String = "", var phone: String = "", var uuid: String = "");