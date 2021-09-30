package com.example.app.controller

import com.example.app.api.APIServer.Request
import org.json.JSONObject

trait APIUtil {

  private val reqJson: Request => Option[JSONObject] =
    (request: Request) => for {
      req <- request.body
    } yield new JSONObject(req)

  def jsonParam[T](param: String, filter: String => Boolean)
                  (exchange: String => T)
                  (implicit request: Request)
  : Option[T] = for {
    x <- reqJson(request).map(_.get(param).toString)
    if x.nonEmpty && filter(x)
  } yield exchange(x)

  def jsonStrParam(param: String, filter: String => Boolean)
                  (implicit request: Request)
  : Option[String] = for {
    x <- reqJson(request).map(_.get(param).toString)
    if x.nonEmpty && filter(x)
  } yield x

}
