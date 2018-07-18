package com.tmindtech.api.lotteryDraw.db

import com.github.pagehelper.Page
import com.tmindtech.api.lotteryDraw.model.PrizeWinnerListDetail
import org.apache.ibatis.annotations.Select
import tk.mybatis.mapper.common.Mapper

interface PrizeWinnerListDetailMapper: Mapper<PrizeWinnerListDetail> {


    @Select("select * from t_prize_winner_list_detail where list_id = #{listId}")
    fun getByListId(listId: Long): Page<PrizeWinnerListDetail>;
}