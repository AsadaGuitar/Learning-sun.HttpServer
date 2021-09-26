package com.example.app.api.home

import com.example.app.api.APIServer.Request
import com.example.app.api.home.MoneyAPI.jsonIntParam
import com.example.app.domain.Cost
import com.example.app.service.{CostService, CostServiceMySQL}

object CostAPI {

  private lazy val service: CostService = new CostServiceMySQL

  def get(implicit request: Request): Option[String] ={

    val userIdJson = jsonIntParam(param = "user_id", _ => true)
    if (userIdJson.isEmpty) return Some("Could not get 'user_id'.")
    println(s"user_id = ${userIdJson.get}")

    val monthJson   = jsonIntParam(param = "month", _ => true)
    if (monthJson.isEmpty) return Some("Could not get 'month'.")
    println(s"month = ${monthJson.get}")

    val cost = new Cost(userIdJson.get, None, None, None, None, monthJson.get)

    service.findOne(cost) match {
      case Right(x) =>
        val r = x.foldLeft("\n")((acc,x) => acc ++ s"${x._1} : ${x._2}\n")
        Some(r)
      case Left(e) => Some(e)
    }
  }

  def post(implicit request: Request): Option[String] ={

    val userIdJson = jsonIntParam(param = "user_id", _ => true)
    if (userIdJson.isEmpty) return Some("Could not get 'user_id'.")

    val monthJson   = jsonIntParam(param = "month", _ => true)
    if (monthJson.isEmpty) return Some("Could not get 'month'.")

    val rentJson    = jsonIntParam(param = "rent", _ => true)
    val cardJson    = jsonIntParam(param = "card", _ => true)
    val otherJson   = jsonIntParam(param = "other", _ => true)
    val friendsJson = jsonIntParam(param = "friends", _ => true)

    val cost =
      new Cost(userIdJson.get, rentJson, cardJson, otherJson, friendsJson, monthJson.get)

    val msg = service.insert(cost)
    Some(msg)
  }

}
