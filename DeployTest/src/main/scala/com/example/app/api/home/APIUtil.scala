package com.example.app.api.home

import com.example.app.api.APIServer.Request
import org.json.JSONObject

trait APIUtil {

  private val reqJson: Request => Option[JSONObject] =
    (request: Request) => for{
    req <- request.body
  } yield new JSONObject(req)

  def jsonIntParam(param: String, filter: String => Boolean)(implicit request: Request)
  : Option[Int] = for {
    x <- reqJson(request).map(_.get(param).toString)
    if x.nonEmpty && filter(x)
  } yield x.toInt

  def jsonStrParam(param: String, filter: String => Boolean)(implicit request: Request)
  : Option[String] = for {
    x <- reqJson(request).map(_.get(param).toString)
    if x.nonEmpty && filter(x)
  } yield x

}
