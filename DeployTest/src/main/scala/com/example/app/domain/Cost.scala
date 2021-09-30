package com.example.app.domain

object Cost {

  def apply(userId: Int, rent: Option[Int], card: Option[Int],
            other: Option[Int], friends: Option[Int], month: Int) = {
    val cost = new Cost
    cost
  }
}


class Cost (){

  var userId: Int = _
  var rent: Option[Int] = _
  var card: Option[Int] = _
  var other: Option[Int] = _
  var friends: Option[Int] = _
  var month: Int = _

  def getUserId = userId
  def getRent = rent
  def getCard = card
  def getOther = other
  def getFriends = friends
  def getMonth = month

  def setUserId(userId: Int): Unit = this.userId = userId
  def setRent(rent: Option[Int]): Unit = this.rent = rent
  def setCard(card: Option[Int]): Unit = this.card = card
  def setOther(other: Option[Int]): Unit = this.other = other
  def setFriends(friends: Option[Int]): Unit = this.friends = friends
  def setMonth(month: Int): Unit = this.month = month
}
