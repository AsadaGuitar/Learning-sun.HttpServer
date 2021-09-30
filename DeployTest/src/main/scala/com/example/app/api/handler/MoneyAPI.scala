package com.example.app.api.handler
import com.example.app.api.APIServer
import com.example.app.api.APIServer.{GET, POST, PUT}
import com.example.app.controller.home.MoneyController

object MoneyAPI extends APIHandler {
  override def handler(request: APIServer.Request, path: Array[String], method: APIServer.RequestMethod)
  : Option[String] = method match {
    case GET =>
      if (path.tail.isEmpty) None
      else {
        val child = path(1)
        if (child.matches("\\d{1,11}")) {
          MoneyController.get(child.toInt)
        }
        else None
      }
    case POST => MoneyController.post(request)
    case PUT => None
  }
}
