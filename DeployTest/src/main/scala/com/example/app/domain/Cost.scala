package com.example.app.domain

class Cost (_userId: Int, _rent: Option[Int], _card: Option[Int],
                    _other: Option[Int], _friends: Option[Int], _month: Int){

  def userId: Int = _userId
  def rent: Option[Int] = _rent
  def card: Option[Int] = _card
  def other: Option[Int] = _other
  def friends: Option[Int] = _friends
  def month: Int = _month
}
