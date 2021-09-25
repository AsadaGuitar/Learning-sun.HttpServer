package com.example.app.api.home

import com.example.app.api.APIServer.Request
import com.example.app.dao.{CommitException, ConnectionException, DMLException, RollbackException, SavepointException}
import com.example.app.service.{UserServiceMySQL, UsersService}
import org.json.JSONObject


object EntryAPI {

  lazy val service: UsersService = new UserServiceMySQL

  def get(): Unit ={
  }

  @throws[java.sql.SQLException]
  def post(request: Request): Unit = {
    println("start EntryAPI.post()")

    val reqJson = for{
      req <- request.body
    } yield new JSONObject(req)

    val jsonName = for{
      x <- reqJson.map(_.get("name").toString)
      if x.nonEmpty
    } yield x

    if (jsonName.isEmpty) {
      println("Could not Found 'name'.")
      return
    }

    val jsonPass = for{
      x <- reqJson.map(_.get("pass").toString)
      if x.nonEmpty
    } yield x

    if (jsonPass.isEmpty) {
      println("Could not Found 'pass'.")
      return
    }

    println("データベースに接続します。")

    try {

      service.setConnection()

      val sp = service.setSavepoint()

      try {
        for {
          name <- jsonName
          pass <- jsonPass
        } yield service.create(name, pass)
      }
      catch {
        case e: DMLException =>
          println("Could not create 'users'.")
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
}

