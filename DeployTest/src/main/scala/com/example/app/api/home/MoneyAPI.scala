package com.example.app.api.home

import com.example.app.api.APIServer.Request
import com.example.app.domain.Money
import com.example.app.service._

object MoneyAPI extends APIUtil {

  private lazy val service: MoneyService = new MoneyServiceMySQL

  def get(userId: Int): Option[String] = service.findAllMoney(userId)

  def post(implicit request: Request): Option[String] = {

    println("start MoneyAPI.post()")

    val userIdJson = jsonIntParam(param = "id", _.matches("\\d{1,4}"))
    if(userIdJson.isEmpty) return Some("Could not get 'user_id'.")

    val savingJson = jsonIntParam(param = "saving", _.matches("\\d{3,10"))
    if(savingJson.isEmpty) return Some("Could not get 'saving'.")

    val walletJson = jsonIntParam(param = "wallet", _.matches("\\d{1,6}"))
    if(walletJson.isEmpty) return Some("Could not get 'wallet'.")

    for {
      userId <- userIdJson
      saving <- savingJson
      wallet <- walletJson
    } yield {
      val money = new Money(userId, saving, wallet)

      service.insert(money)
    }
  }

  def put(request: Request): Unit = {

  }
}
