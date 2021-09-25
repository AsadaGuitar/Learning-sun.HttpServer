package com.example.app.service

import com.example.app.dao.{DatabaseAccessory, MySQLAccessory}

import java.sql.SQLException

trait MoneyService extends DatabaseAccessory{

  @throws[SQLException]
  def create(userId: Int, saving: Int, wallet: Int): Int ={
    val sqlFormat = "INSERT INTO money(user_id, saving, wallet) VALUES(%d, %d, %d);"
    val sql = sqlFormat.format(userId, saving, wallet)
    executeUpdate(sql)
  }
}

class MoneyServiceMySQL extends MySQLAccessory(false) with MoneyService