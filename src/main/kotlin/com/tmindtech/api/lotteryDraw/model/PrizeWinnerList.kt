package com.tmindtech.api.lotteryDraw.model

import com.tmindtech.api.base.model.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "t_prize_winner_list")
class PrizeWinnerList(var published: Boolean = false): BaseEntity()