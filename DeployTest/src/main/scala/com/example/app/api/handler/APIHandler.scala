package com.example.app.api.handler

import com.example.app.api.APIServer.{Request, RequestMethod}
import org.apache.log4j.Logger

trait APIHandler extends JsonObjectMapper {

  def handler(request: Request, path: Array[String], method: RequestMethod): Option[String]

  def get(implicit request: Request): Option[String]

  def post(implicit request: Request): Option[String]

  def put(implicit request: Request): Option[String]
}

trait LoggableAPIHandler extends APIHandler {
  val log = Logger.getLogger(super.getClass.getName)

  abstract override def handler(request: Request, path: Array[String], method: RequestMethod)
  : Option[String] = {
    log.info(s"Start Method ${super.getClass.getName}.handler.")
    val result = super.handler(request, path, method)
    log.info(s"End Method ${super.getClass.getName}.handler")
    result
  }

  private def loggingMethod(methodName: String)(method: Request => Option[String])
                           (implicit request: Request): Option[String] ={
    log.info(s"Start Method ${super.getClass.getName}.$methodName")
    val result = method(request)
    log.info(s"End Method ${super.getClass.getName}.$methodName")
    result
  }

  abstract override def get(implicit request: Request)
  : Option[String] = loggingMethod("get")(super.get(_))

  abstract override def post(implicit request: Request)
  : Option[String] = loggingMethod("post")(super.post(_))

  abstract override def put(implicit request: Request)
  : Option[String] = loggingMethod("put")(super.put(_))
}