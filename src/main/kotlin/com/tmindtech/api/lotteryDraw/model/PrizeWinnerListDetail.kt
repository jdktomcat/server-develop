package com.tmindtech.api.lotteryDraw.model

import com.tmindtech.api.base.model.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "t_prize_winner_list_detail")
class PrizeWinnerListDetail(val listId: Long? = 0L, val userId: Long? = 0L): BaseEntity()