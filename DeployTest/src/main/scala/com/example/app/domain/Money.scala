package com.example.app.domain

import javax.persistence.{Column, Entity, Id, Table}

object Money {

  def apply(userId: Int, saving: Int, wallet: Int): Money = {
    val money = new Money
    money.setUserId(userId)
    money.setSaving(saving)
    money.setWallet(wallet)
    money
  }
}

@Entity
@Table(name = "money")
class Money {

  @Id
  @Column(name = "user_id")
  var userId: Int = _

  @Column(name = "saving")
  var saving: Int = _

  @Column(name = "wallet")
  var wallet: Int = _

  def getUserId = userId
  def getSaving = saving
  def getWallet = wallet

  def setUserId(userId: Int): Unit = this.userId = userId
  def setSaving(saving: Int): Unit = this.saving = saving
  def setWallet(wallet: Int): Unit = this.wallet = wallet
}
