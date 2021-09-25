package com.example.app.service

import com.example.app.dao.{DatabaseAccessory, MySQLAccessory}

trait UsersService extends DatabaseAccessory {

  @throws[java.sql.SQLException]
  def create(name: String, pass: String): Int ={
    val sqlFormat = "INSERT INTO users(name, password) VALUES('%s', '%s');"
    val sql = sqlFormat.format(name, pass)
    executeUpdate(sql)
  }
}

class UserServiceMySQL extends MySQLAccessory(false) with UsersService