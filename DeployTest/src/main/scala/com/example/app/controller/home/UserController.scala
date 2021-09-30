package com.example.app.controller.home

import com.example.app.api.APIServer.Request
import com.example.app.controller.APIUtil
import com.example.app.domain.User
import com.example.app.service.{UserServiceMySQL, UsersService}

object UserController extends APIUtil {

  private lazy val service: UsersService = new UserServiceMySQL

  def get(): Unit = {
  }

  def post(implicit request: Request): Option[String] = {
    println("start EntryAPI.post()")

    val jsonName = jsonStrParam(param = "name", _ => true)
    if (jsonName.isEmpty) return Some("Could not Found 'name'.")

    val jsonPass = jsonStrParam(param = "pass", _ => true)
    if (jsonPass.isEmpty) return Some("Could not Found 'pass'.")

    for {
      name <- jsonName
      pass <- jsonPass
    } yield {
      val user = new User
      user.setName(name)
      user.setPass(pass)
      service.insert(user)
    }
  }
}
