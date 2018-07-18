package com.tmindtech.api.lotteryDraw.service

import com.github.pagehelper.PageHelper
import com.tmindtech.api.account.Config
import com.tmindtech.api.base.exception.AwesomeException
import com.tmindtech.api.base.model.DataList
import com.tmindtech.api.lotteryDraw.db.ContactInfoMapper
import com.tmindtech.api.lotteryDraw.db.PrizeWinnerListDetailMapper
import com.tmindtech.api.lotteryDraw.db.PrizeWinnerListMapper
import com.tmindtech.api.lotteryDraw.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PrizeWinnerListService() {

    @Autowired
    lateinit var prizeWinnerListMapper: PrizeWinnerListMapper;

    @Autowired
    lateinit var prizeWinnerListDetailMapper: PrizeWinnerListDetailMapper;

    @Autowired
    lateinit var contactInfoMappper: ContactInfoMapper;

    fun getPrizeWinnerList(published: Boolean, offset: Int, limit: Int): PrizeWinnerListRes {
        var prizeWinnerListRes: PrizeWinnerListRes = PrizeWinnerListRes();
        var id: Long?;
        if (published) {
            id = prizeWinnerListMapper.selectLastPublishedListId()
        } else {
            id = prizeWinnerListMapper.selectLastUnPublishedListId()
        }
        PageHelper.offsetPage<Any>(offset, limit);
        id ?: return PrizeWinnerListRes(0, DataList<ContactInfo>(0, 0, 0, ArrayList<ContactInfo>()));
        var prizeWinnerListDeailPage = prizeWinnerListDetailMapper.getByListId(id);
        var contactInfoList = ArrayList<ContactInfo>();
        prizeWinnerListDeailPage.forEach({
            var contactInfo = contactInfoMappper.selectByPrimaryKey(it.userId);
            contactInfoList.add(contactInfo);
        })
        var res = DataList<ContactInfo>(prizeWinnerListDeailPage.startRow, prizeWinnerListDeailPage.result.size,
                prizeWinnerListDeailPage.total, contactInfoList);
        prizeWinnerListRes.listId = id;
        prizeWinnerListRes.dataList = res;
        return prizeWinnerListRes;
    }


    fun isContactInTheCurrentPrizeWinnerList(contactId: Long?): Boolean {
        if(contactId == null) {
            return false;
        }
        var unPublishedListId: Long? = initializeNewList();
        var toSelect: PrizeWinnerListDetail = PrizeWinnerListDetail(unPublishedListId, contactId);

        if(prizeWinnerListDetailMapper.select(toSelect).size > 0) {
            return true;
        } else {
            return false;
        }
    }

    fun publishPrizeWinnerList(listId: Long) {
        checkListId(listId)
        var prizeWinnerListToUpdate = PrizeWinnerList(true);
        prizeWinnerListToUpdate.id = listId;
        prizeWinnerListMapper.updateByPrimaryKeySelective(prizeWinnerListToUpdate);
    }


    fun addPrizeWinner(addPrizeWinnerReq: AddPrizeWinnerReq) {
        checkContactId(addPrizeWinnerReq.userId)
        var listId = initializeNewList();
        var contactToSelect = PrizeWinnerListDetail(listId, addPrizeWinnerReq.userId);
        if(prizeWinnerListDetailMapper.select(contactToSelect).size > 0) {
            throw AwesomeException(Config.ERROR_USER_EXISTED);
        }
        prizeWinnerListDetailMapper.insertSelective(PrizeWinnerListDetail(listId, addPrizeWinnerReq.userId));

    }

    fun delPrizwWinner(addPrizeWinnerReq: AddPrizeWinnerReq) {
        checkContactId(addPrizeWinnerReq.userId)
        var listId = initializeNewList()
        var prizeWinnerListDetailToDel = PrizeWinnerListDetail(listId, addPrizeWinnerReq.userId);
        prizeWinnerListDetailMapper.delete(prizeWinnerListDetailToDel);
    }

    /**
     * 获取最新的未发布列表id,没有则生成一个新的
     */
    fun initializeNewList(): Long? {
        var prizeWinnerList = prizeWinnerListMapper.selectLast();
        if (prizeWinnerList == null || prizeWinnerList.published) {
            var prizeWinnerListToInsert = PrizeWinnerList(false);
            prizeWinnerListMapper.insertSelective(prizeWinnerListToInsert);
            return prizeWinnerListToInsert.id
        } else {
            return prizeWinnerList.id
        }
    }

    fun checkListId(listId: Long) {
        prizeWinnerListMapper.selectByPrimaryKey(listId) ?: throw AwesomeException(Config.ERROR_INVALID_ID)
    }

    fun checkContactId(contactId: Long?) {
        contactInfoMappper.selectByPrimaryKey(contactId) ?: throw AwesomeException(Config.ERROR_INVALID_ID)
    }


}