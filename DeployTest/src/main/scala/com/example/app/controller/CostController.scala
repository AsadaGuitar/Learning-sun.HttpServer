package com.example.app.controller

import com.example.app.api.handler.JsonObjectMapper
import com.example.app.api.APIServer._
import com.example.app.domain.Cost
import com.example.app.service.CostService
import com.example.app.session.SessionException

class CostController extends JsonObjectMapper{

  val service = new CostService

  private def makeJson(cost: Cost): String ={
    s"{\"user_id\":\"${cost.getUserId}\",\"month\":\"${cost.getMonth}\",\"rent\":\"${cost.getRent}\"," +
      s"\"card\":\"${cost.getCard}\",\"other\":\"${cost.getOther}\",\"friends\":\"${cost.getFriends}\"}"
  }

  def get(userId: Int): Either[SessionException, Option[String]] = {
    println(getClass.getName ++ ".get")
    service.findOne(userId) match {
      case Right(x) => x match {
        case Some(cost) =>
          val json = makeJson(cost)
          Right(Some(json))
        case None => Right(None)
      }
      case Left(e) => Left(e)
    }
  }

  def post(implicit request: Request): Either[SessionException, Option[String]] = {

    val userId = getJsonParam("user_id", _.matches("\\d{1,6}")).map(_.toInt)
    val month = getJsonParam("month", _.matches("\\d{1,6}")).map(_.toInt)
    val rent = getJsonParam("rent", _.matches("\\d{1,6}")).map(_.toInt)
    val card = getJsonParam("card", _.matches("\\d{1,6}")).map(_.toInt)
    val other = getJsonParam("other", _.matches("\\d{1,6}")).map(_.toInt)
    val friends = getJsonParam("friends", _.matches("\\d{1,6}")).map(_.toInt)

    val result = for {
      u  <- userId
      m  <- month
    } yield {
      val cost = Cost(u, m)
      if (rent.isDefined) cost.setRent(rent.get)
      if (card.isDefined) cost.setCard(card.get)
      if (other.isDefined) cost.setOther(other.get)
      if (friends.isDefined) cost.setFriends(friends.get)
      service.insert(cost)
    }

    result match {
      case Some(x) => x match {
        case Right(_) => Right(None)
        case Left(e) => Left(e)
      }
      case None => Right(None)
    }
  }

  def put(implicit request: Request): Option[String] = None
}
