package com.example.app.api.handler
import com.example.app.api.APIServer
import com.example.app.api.APIServer.{GET, POST, PUT}
import com.example.app.controller.home.CostController

object CostAPI extends APIHandler {
  override def handler(request: APIServer.Request, path: Array[String], method: APIServer.RequestMethod)
  : Option[String] = method match {
    case GET => CostController.get(request)
    case POST => CostController.post(request)
    case PUT => None
  }
}
