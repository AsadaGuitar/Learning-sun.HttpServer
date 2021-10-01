package com.example.app.api.handler

import com.example.app.api.APIServer.Request
import org.json.JSONObject

trait JsonObjectMapper {

  private val reqJson: Request => Option[JSONObject] =
    (request: Request) => for {
      req <- request.body
    } yield new JSONObject(req)

  def getJsonParam(key: String, filter: String => Boolean)
                  (implicit request: Request)
  : Option[String] = for {
    x <- reqJson(request).map(_.get(key).toString)
    if x.nonEmpty && filter(x)
  } yield x

}
