package com.example.app.controller

import com.example.app.api.APIServer.Request
import com.example.app.api.handler.JsonObjectMapper
import com.example.app.domain.User
import com.example.app.service.UsersService
import com.example.app.session.SessionException

class UserController extends JsonObjectMapper{

  val service = new UsersService

  def get(implicit request: Request): Option[String] = None

  def post(implicit request: Request): Either[SessionException,Option[String]] ={

    val jsonName = getJsonParam("name", _ => true)
    val jsonPass = getJsonParam("pass", _ => true)

    val result = for {
      name <- jsonName
      pass <- jsonPass
    } yield {
      val user = User(0, name, pass)
      service.insert(user)
    }

    result match {
      case Some(x) => x match {
        case Right(_) => Right(None)
        case Left(e) => Left(e)
      }
      case None => Right(None)
    }
  }

  def put(implicit request: Request): Option[String] = None

}
