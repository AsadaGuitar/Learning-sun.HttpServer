package com.example.app.api.handler

import com.example.app.api.APIServer.{Request, RequestMethod}

trait APIHandler {
  def handler(request: Request, path: Array[String], method: RequestMethod): Option[String]
}
