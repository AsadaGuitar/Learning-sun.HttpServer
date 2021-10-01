package com.example.app.api.handler
import com.example.app.api.APIServer
import com.example.app.api.APIServer.{GET, POST, PUT}
import com.example.app.domain.Money
import com.example.app.service.MoneyServiceMySQL

class MoneyAPIHandler extends APIHandler {

  val service = new MoneyServiceMySQL

  override def handler(request: APIServer.Request, path: Array[String], method: APIServer.RequestMethod)
  : Option[String] = method match {
    case GET =>
      if (path.tail.isEmpty) None
      else {
        val child = path(1)
        if (child.matches("\\d{1,11}")) {
          get(request)
        }
        else None
      }
    case POST => post(request)
    case PUT => None
  }

  override def get(implicit request: APIServer.Request): Option[String] = None

  override def post(implicit request: APIServer.Request): Option[String] ={
    val userIdJson = getJsonParam("id", _.matches("\\d{1,4}")).map(_.toInt)
    if (userIdJson.isEmpty) return Some("Could not get 'user_id'.")

    val savingJson = getJsonParam("saving", _.matches("\\d{3,10")).map(_.toInt)
    if (savingJson.isEmpty) return Some("Could not get 'saving'.")

    val walletJson = getJsonParam("wallet", _.matches("\\d{1,6}")).map(_.toInt)
    if (walletJson.isEmpty) return Some("Could not get 'wallet'.")

    for {
      userId <- userIdJson
      saving <- savingJson
      wallet <- walletJson
    } yield {
      val money = Money(userId, saving, wallet)

      service.insert(money)
    }
  }

  override def put(implicit request: APIServer.Request): Option[String] = None
}

class MoneyLoggingHandler extends MoneyAPIHandler with LoggableAPIHandler