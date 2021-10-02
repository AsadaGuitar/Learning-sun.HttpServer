package com.example.app.api.handler
import com.example.app.api.APIServer._
import com.example.app.controller.CostController
import com.example.app.session.SessionException

class LoggingCostAPIHandler extends CostAPIHandler with LoggableAPIHandler

class CostAPIHandler extends APIHandler {

  val controller = new CostController

  override def handler(request: Request, path: Array[String], method: RequestMethod)
  : Either[SessionException, Option[String]] = method match {
    case GET => controller.get(path.head.toInt)
    case POST => controller.post(request)
    case PUT => Right(None)
  }
}

