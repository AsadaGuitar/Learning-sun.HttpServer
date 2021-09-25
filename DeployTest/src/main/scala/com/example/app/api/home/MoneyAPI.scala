package com.example.app.api.home

import com.example.app.api.APIServer.Request
import com.example.app.dao.{CommitException, ConnectionException, DMLException, RollbackException, SavepointException}
import com.example.app.service._
import org.json.JSONObject

object MoneyAPI {

  lazy val service: MoneyService = new MoneyServiceMySQL

  def get(request: Request): Unit ={

  }

  def post(request: Request): Unit = {

    println("start MoneyAPI.post()")

    val reqJson = for{
      req <- request.body
    } yield new JSONObject(req)

    val userIdJson = for{
      x <- reqJson.map(_.get("id").toString)
      if x.nonEmpty && x.matches("\\d{1,4}")
    } yield x.toInt

    if(userIdJson.isEmpty) {
      println("Could not get 'user_id'.")
      return
    }

    val savingJson = for{
      x <- reqJson.map(_.get("saving").toString)
      if x.nonEmpty && x.matches("\\d{1,10}")
    } yield x.toInt

    if(savingJson.isEmpty) {
      println("Could not get 'saving'.")
      return
    }

    val walletJson = for{
      x <- reqJson.map(_.get("wallet").toString)
      if x.nonEmpty && x.matches("\\d{1,6}")
    } yield x.toInt

    if(walletJson.isEmpty) {
      println("Could not get 'wallet'.")
      return
    }

    try {

      service.setConnection()

      val sp = service.setSavepoint()

      try {
        for {
          userId <- userIdJson
          saving <- savingJson
          wallet <- walletJson
        } yield service.create(userId,saving,wallet)
      }
      catch {
        case e: DMLException =>
          println(e.getMessage)
          service.rollBack(sp)
      }

      service.commit()
    }
    catch {
      case e1: ConnectionException => println(s"Could not to connect database.\n${e1.getMessage}")
      case e2: SavepointException => println(s"Could not to get 'Savepoint'.\n${e2.getMessage}")
      case e3: RollbackException => println(s"Could not to rollback\n${e3.getMessage}")
      case e4: CommitException => println(s"Could not to commit\n${e4.getMessage}")
    }
    finally {
      service.close()
    }

    println("コミットしました。")
  }

  def put(request: Request): Unit = {

  }
}
