package com.example.app.api.handler
import com.example.app.api.APIServer
import com.example.app.api.APIServer.{GET, POST, PUT}
import com.example.app.domain.Cost
import com.example.app.service.CostServiceMySQL

class CostAPIHandler extends APIHandler {

  val service = new CostServiceMySQL

  override def handler(request: APIServer.Request, path: Array[String], method: APIServer.RequestMethod)
  : Option[String] = method match {
    case GET => get(request)
    case POST => post(request)
    case PUT => None
  }

  override def get(implicit request: APIServer.Request): Option[String] = {
    val userIdJson = getJsonParam("user_id", _.matches("\\d{1,6}")).map(_.toInt)
    if (userIdJson.isEmpty) return Some("Could not get 'user_id'.")

    val monthJson = getJsonParam("month", _.matches("\\d{1,6}")).map(_.toInt)
    if (monthJson.isEmpty) return Some("Could not get 'month'.")

    val cost = Cost(userIdJson.get, None, None, None, None, monthJson.get)

    service.findOne(cost) match {
      case Right(x) =>
        val r = x.foldLeft("\n")((acc, x) => acc ++ s"${x._1} : ${x._2}\n")
        Some(r)
      case Left(e) => Some(e)
    }
  }

  override def post(implicit request: APIServer.Request): Option[String] = {
    val userIdJson = getJsonParam("user_id", _.matches("\\d{1,6}")).map(_.toInt)
    if (userIdJson.isEmpty) return Some("Could not get 'user_id'.")

    val monthJson = getJsonParam("month", _.matches("\\d{1,6}")).map(_.toInt)
    if (monthJson.isEmpty) return Some("Could not get 'month'.")

    val rentJson = getJsonParam("rent", _.matches("\\d{1,6}")).map(_.toInt)
    val cardJson = getJsonParam("card", _.matches("\\d{1,6}")).map(_.toInt)
    val otherJson = getJsonParam("other", _.matches("\\d{1,6}")).map(_.toInt)
    val friendsJson = getJsonParam("friends", _.matches("\\d{1,6}")).map(_.toInt)

    val cost = Cost(userIdJson.get, rentJson, cardJson, otherJson, friendsJson, monthJson.get)

    val msg = service.insert(cost)
    Some(msg)
  }

  override def put(implicit request: APIServer.Request): Option[String] = None
}

class CostLoggingAPIHandler extends CostAPIHandler with LoggableAPIHandler