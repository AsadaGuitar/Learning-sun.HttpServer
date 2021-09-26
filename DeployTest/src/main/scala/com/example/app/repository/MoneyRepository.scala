package com.example.app.repository

import com.example.app.dao.{DMLException, DatabaseAccessory, MySQLAccessory}
import com.example.app.domain.Money

import java.sql.ResultSet

object MoneyRepository {

  case class CreateUtilityCostsSQL(column: String, value: String)
}

trait MoneyRepository extends DatabaseAccessory{

  protected def readALLMoney(userId: Int): Either[DMLException,ResultSet] ={

    val sql = s"SELECT (saving + wallet) AS total FROM money WHERE user_id = ${userId};"

    try {
      val rs = executeQuery(sql)
      Right(rs)
    }
    catch {
      case e: DMLException => Left(e)
    }
  }

  protected def create(money: Money): Either[DMLException,Int] ={

    val sql = "INSERT INTO money(user_id, saving, wallet)" +
      s" VALUES(${money.userId}, ${money.saving}, ${money.wallet});"

    try {
      val rs = executeUpdate(sql)
      Right(rs)
    }
    catch {
      case e: DMLException => Left(e)
    }
  }
}

class MoneyRepositoryMySQL extends MySQLAccessory(false) with MoneyRepository
