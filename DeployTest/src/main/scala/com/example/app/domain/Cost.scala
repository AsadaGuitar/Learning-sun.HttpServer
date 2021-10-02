package com.example.app.domain

import javax.persistence.{Column, Entity, Id, Table}

object Cost {

  def apply(userId: Int, month: Int) = {
    val cost = new Cost
    cost.setUserId(userId)
    cost.setMonth(month)
    cost
  }

  def apply(userId: Int, rent: Int, card: Int, other: Int,
            friends: Int, month: Int) = {
    val cost = new Cost
    cost.setUserId(userId)
    cost.setRent(rent)
    cost.setCard(card)
    cost.setOther(other)
    cost.setFriends(friends)
    cost.setMonth(month)
    cost
  }
}

@Entity
@Table(name = "utility_costs")
class Cost {

  @Id
  @Column(name = "user_id")
  var userId: Int = _

  @Column(name = "rent")
  var rent: Int = _

  @Column(name = "card")
  var card: Int = _

  @Column(name = "other")
  var other: Int = _

  @Column(name = "friends")
  var friends: Int = _

  @Column(name = "month")
  var month: Int = _

  def getUserId = userId
  def getRent = rent
  def getCard = card
  def getOther = other
  def getFriends = friends
  def getMonth = month

  def setUserId(userId: Int): Unit = this.userId = userId
  def setRent(rent: Int): Unit = this.rent = rent
  def setCard(card: Int): Unit = this.card = card
  def setOther(other: Int): Unit = this.other = other
  def setFriends(friends: Int): Unit = this.friends = friends
  def setMonth(month: Int): Unit = this.month = month
}
