package com.example.app.api.handler
import com.example.app.api.APIServer
import com.example.app.api.APIServer.{GET, POST, PUT, Request}
import com.example.app.domain.User
import com.example.app.service.UserServiceMySQL

class UserAPIHandler extends APIHandler {

  val service = new UserServiceMySQL

  override def handler(request: Request, path: Array[String], method: APIServer.RequestMethod)
  : Option[String] = path match {
    case p if p.isEmpty => method match {
      case GET => None
      case POST => post(request)
      case PUT => None
    }
    case _ => None
  }

  override def get(implicit request: Request): Option[String] = None

  override def post(implicit request: Request): Option[String] ={

    val jsonName = getJsonParam("name", _ => true)
    if (jsonName.isEmpty) return Some("Could not Found 'name'.")

    val jsonPass = getJsonParam("pass", _ => true)
    if (jsonPass.isEmpty) return Some("Could not Found 'pass'.")

    for {
      name <- jsonName
      pass <- jsonPass
    } yield {
      val user = User(0, name, pass)
      service.insert(user)
    }
  }

  override def put(implicit request: Request): Option[String] = None
}

class UserLoggingHandler extends UserAPIHandler with LoggableAPIHandler