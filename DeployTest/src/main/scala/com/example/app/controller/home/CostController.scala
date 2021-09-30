package com.example.app.controller.home

import com.example.app.api.APIServer.Request
import com.example.app.controller.APIUtil
import com.example.app.domain.Cost
import com.example.app.service.{CostService, CostServiceMySQL}

object CostController extends APIUtil {

  private lazy val service: CostService = new CostServiceMySQL

  def get(implicit request: Request): Option[String] = {

    val userIdJson = jsonParam(param = "user_id", _ => true)(_.toInt)
    if (userIdJson.isEmpty) return Some("Could not get 'user_id'.")
    println(s"user_id = ${userIdJson.get}")

    val monthJson = jsonParam(param = "month", _ => true)(_.toInt)
    if (monthJson.isEmpty) return Some("Could not get 'month'.")
    println(s"month = ${monthJson.get}")

    val cost = Cost(userIdJson.get, None, None, None, None, monthJson.get)

    service.findOne(cost) match {
      case Right(x) =>
        val r = x.foldLeft("\n")((acc, x) => acc ++ s"${x._1} : ${x._2}\n")
        Some(r)
      case Left(e) => Some(e)
    }
  }

  def post(implicit request: Request): Option[String] = {

    val userIdJson = jsonParam(param = "user_id", _ => true)(_.toInt)
    if (userIdJson.isEmpty) return Some("Could not get 'user_id'.")

    val monthJson = jsonParam(param = "month", _ => true)(_.toInt)
    if (monthJson.isEmpty) return Some("Could not get 'month'.")

    val rentJson = jsonParam(param = "rent", _ => true)(_.toInt)
    val cardJson = jsonParam(param = "card", _ => true)(_.toInt)
    val otherJson = jsonParam(param = "other", _ => true)(_.toInt)
    val friendsJson = jsonParam(param = "friends", _ => true)(_.toInt)

    val cost = Cost(userIdJson.get, rentJson, cardJson, otherJson, friendsJson, monthJson.get)

    val msg = service.insert(cost)
    Some(msg)
  }

}
