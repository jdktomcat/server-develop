package com.tmindtech.api.lotteryDraw.db

import com.tmindtech.api.lotteryDraw.model.PrizeWinnerList
import org.apache.ibatis.annotations.Select
import tk.mybatis.mapper.common.Mapper
import javax.persistence.Entity
import javax.persistence.Table

@Table( name = "t_prize_winner_list")
@Entity
interface PrizeWinnerListMapper: Mapper<PrizeWinnerList> {

    @Select("select * from t_prize_winner_list ORDER BY create_time desc LIMIT 0, 1")
    fun selectLast(): PrizeWinnerList?;

    @Select("select id from t_prize_winner_list where published = 1 ORDER BY create_time desc LIMIT 0, 1")
    fun selectLastPublishedListId(): Long?;

    @Select("select id from t_prize_winner_list where published = 0 ORDER BY create_time desc LIMIT 0, 1")
    fun selectLastUnPublishedListId(): Long?;

}