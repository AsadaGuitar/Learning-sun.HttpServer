package com.example.app.api.home

import com.example.app.api.APIServer.Request
import com.example.app.domain.User
import com.example.app.service.{UserServiceMySQL, UsersService}


object EntryAPI extends APIUtil {

  private lazy val service: UsersService = new UserServiceMySQL

  def get(): Unit ={
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

      val user = new User(None, name, pass)
      service.insert(user)
    }
  }
}

