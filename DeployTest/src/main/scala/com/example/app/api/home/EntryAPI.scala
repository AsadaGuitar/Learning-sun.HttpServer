package com.example.app.api.home

import com.example.app.api.APIServer.Request
import com.example.app.dao.DatabaseAccessory
import org.json.JSONObject

import java.sql.Savepoint


object EntryAPI {

  private val url = "jdbc:mysql://localhost:3306/money_keep"
  private val username = "root"
  private val password = "Asd18894"

  def get(request: Request): Unit ={


  }

  @throws[java.sql.SQLException]
  def post(request: Request): Unit = {

    val reqJson = for{
      req <- request.body
    } yield new JSONObject(req)

    val jsonName = reqJson.map(_.get("name"))
    val name = jsonName match {
      case Some(x) => x.toString
      case None =>
        println("Not Found 'name'.")
        return
    }

    val jsonPass = reqJson.map(_.get("pass"))
    val pass = jsonPass match {
      case Some(x) => x.toString
      case None =>
        println("Not Found 'pass'.")
        return
    }


    import java.sql.SQLException

    val sqlFormat = "INSERT INTO users(name, password) VALUES('%s', '%s');"

    lazy val database: Either[SQLException, DatabaseAccessory] = try {
      val d = DatabaseAccessory(url,username,password)
      Right(d)
    } catch {case e: SQLException =>
      println("Could not access to Database.")
      Left(e)
    }

    lazy val savepoint: Either[SQLException, Either[SQLException, Savepoint]] = try {
      for{d <- database} yield try {
        val s = d.setSavepoint()
        Right(s)
      } catch {case e: SQLException =>
        println("Could not get Savepoint.")
        Left(e)}
    }

    println("データベースに接続します。")
    for {
      dao <- database
      sp  <- savepoint.flatten
    } yield {
      try {
        val sql = sqlFormat.format(name, pass)
        dao.executeUpdate(sql)
      }
      catch {
        case _: SQLException =>
          println("Could not commit.")
          dao.rollBack(sp)
      }
    }
    println("コミットしました。")
  }
}

