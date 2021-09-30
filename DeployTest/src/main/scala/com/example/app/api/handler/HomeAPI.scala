package com.example.app.api.handler

import com.example.app.api.APIServer.{Request, RequestMethod}

object HomeAPI extends APIHandler {

  override def handler(request: Request, path: Array[String], method: RequestMethod)
  : Option[String] = path match {
    case p if p.isEmpty => None
    case p => p.head match {
      case "user"  => UserAPI.handler(request, path.tail, method)
      case "money" => MoneyAPI.handler(request, path.tail, method)
      case "cost"  => CostAPI.handler(request, path.tail, method)
      case _ => None
    }
  }

//  def homeHandler(path: Array[String], method: RequestMethod, request: Request)
//  : Option[String] = path match {
//    case x if x.isEmpty => None
//    case x => x.head match {
//
//
//
//    }
//  }
}
