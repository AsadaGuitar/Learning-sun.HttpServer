package com.example.app.service

import com.example.app.dao.{DMLException, DatabaseAccessory, MySQLAccessory}

import java.sql.ResultSet

trait MoneyService extends DatabaseAccessory{

  def readALLMoney(userId: Int): Either[DMLException,ResultSet] ={

    val sqlFormat = "SELECT (saving + wallet) AS total FROM money WHERE user_id = %d;"
    val sql = sqlFormat.format(userId)

    try {
      val rs = executeQuery(sql)
      Right(rs)
    }
    catch {
      case e: DMLException => Left(e)
    }
  }

  @throws[DMLException]
  def create(userId: Int, saving: Int, wallet: Int): Either[DMLException,Int] ={
    val sqlFormat = "INSERT INTO money(user_id, saving, wallet) VALUES(%d, %d, %d);"
    val sql = sqlFormat.format(userId, saving, wallet)
    try {
      val rs = executeUpdate(sql)
      Right(rs)
    }
    catch {
      case e: DMLException => Left(e)
    }
  }
}

class MoneyServiceMySQL extends MySQLAccessory(false) with MoneyService