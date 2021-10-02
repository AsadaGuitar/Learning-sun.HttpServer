package com.example.app.controller

import com.example.app.api.APIServer
import com.example.app.api.handler.JsonObjectMapper
import com.example.app.domain.Money
import com.example.app.service.MoneyService
import com.example.app.session.SessionException

class MoneyController extends JsonObjectMapper {

  val service = new MoneyService

  def get(userId: String): Either[SessionException, Option[String]] = Right(None)

  private def makeJson(money: Money) ={
    s"{\"user_id\":\"${money.getUserId}\",\"saving\":\"${money.getSaving}\",\"wallet\":\"${money.getWallet}\"}"
  }

  def get(): Either[SessionException, Option[String]] = for {
    result <- service.selectAll()
  } yield {
    val moneyJson = result.foldLeft("")((acc,x) => acc ++ makeJson(x) ++ "\n")
    Some(moneyJson)
  }

  def post(implicit request: APIServer.Request): Either[SessionException,Option[String]] = {

    val userIdJson = getJsonParam("id", _.matches("\\d{1,4}")).map(_.toInt)
    val savingJson = getJsonParam("saving", _.matches("\\d{3,10")).map(_.toInt)
    val walletJson = getJsonParam("wallet", _.matches("\\d{1,6}")).map(_.toInt)

    val result = for {
      userId <- userIdJson
      saving <- savingJson
      wallet <- walletJson
    } yield {
      val money = Money(userId, saving, wallet)
      service.insert(money)
    }

    result match {
      case Some(x) => x match {
        case Right(_) => Right(None)
        case Left(e) => Left(e)
      }
      case None => Right(None)
    }
  }

  def put(implicit request: APIServer.Request): Either[SessionException,Option[String]] = Right(None)

}
