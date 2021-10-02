package com.example.app.api.handler
import com.example.app.api.APIServer
import com.example.app.api.APIServer.{GET, POST, PUT}
import com.example.app.controller.MoneyController
import com.example.app.session.SessionException

class LoggingMoneyAPIHandler extends MoneyAPIHandler with LoggableAPIHandler

class MoneyAPIHandler extends APIHandler {

  val controller = new MoneyController

  override def handler(request: APIServer.Request, path: Array[String], method: APIServer.RequestMethod)
  : Either[SessionException, Option[String]] = method match {
    case GET =>
      if (path.isEmpty) controller.get()
      else {
        val child = path(1)
        if (child.matches("\\d{1,11}")) {
          controller.get(child)
        }
        else Right(None)
      }
    case POST => controller.post(request)
    case PUT => Right(None)
  }
}

