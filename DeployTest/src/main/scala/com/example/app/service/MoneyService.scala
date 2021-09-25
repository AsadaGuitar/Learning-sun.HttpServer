package com.example.app.service

import com.example.app.dao.{DMLException, DatabaseAccessory, MySQLAccessory}

import java.sql.ResultSet

trait MoneyService extends DatabaseAccessory{

  @throws[DMLException]
  def readALLMoney(userId: Int): Either[DMLException,ResultSet] ={

    val sql = s"SELECT (saving + wallet) AS total FROM money WHERE user_id = $userId;"

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

    val sql = s"INSERT INTO money(user_id, saving, wallet) VALUES($userId, $saving, $wallet);"

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
