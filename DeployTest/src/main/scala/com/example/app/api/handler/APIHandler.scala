package com.example.app.api.handler

import com.example.app.api.APIServer.{Request, RequestMethod}
import com.example.app.session.SessionException
import org.apache.log4j.Logger

trait APIHandler extends JsonObjectMapper {

  def handler(request: Request, path: Array[String], method: RequestMethod)
  : Either[SessionException, Option[String]]

}

trait LoggableAPIHandler extends APIHandler {
  val log = Logger.getLogger(super.getClass.getName)

  abstract override def handler(request: Request, path: Array[String], method: RequestMethod)
  : Either[SessionException, Option[String]] = {
    log.info(s"Start Method ${super.getClass.getName}.handler.")
    val result = super.handler(request, path, method)
    log.info(s"End Method ${super.getClass.getName}.handler")
    result
  }
}