package com.example.app.api.handler
import com.example.app.api.APIServer
import com.example.app.api.APIServer.{GET, POST, PUT}
import com.example.app.controller.home.UserController

object UserAPI extends APIHandler {
  override def handler(request: APIServer.Request, path: Array[String], method: APIServer.RequestMethod)
  : Option[String] = path match {
    case p if p.isEmpty => method match {
      case GET => None
      case POST => UserController.post(request)
      case PUT => None
    }
    case _ => None
  }
}
