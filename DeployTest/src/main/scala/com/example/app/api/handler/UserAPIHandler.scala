package com.example.app.api.handler
import com.example.app.api.APIServer
import com.example.app.api.APIServer.{GET, POST, PUT, Request}
import com.example.app.controller.UserController
import com.example.app.session.SessionException


class LoggingUserAPIHandler extends UserAPIHandler with LoggableAPIHandler

class UserAPIHandler extends APIHandler {

  val controller = new UserController

  override def handler(request: Request, path: Array[String], method: APIServer.RequestMethod)
  : Either[SessionException, Option[String]] = path match {
    case p if p.isEmpty => method match {
      case GET => Right(None)
      case POST => controller.post(request)
      case PUT => Right(None)
    }
    case _ => Right(None)
  }
}
