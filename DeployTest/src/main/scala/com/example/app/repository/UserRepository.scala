package com.example.app.repository

import com.example.app.dao.{DMLException, DatabaseAccessory, MySQLAccessory}
import com.example.app.domain.User

trait UserRepository extends DatabaseAccessory {

  protected def create(user: User): Either[DMLException, Int] ={
    val sql = s"INSERT INTO users(name, password) VALUES('${user.name}', '${user.pass}');"
    try {
      Right(executeUpdate(sql))
    }
    catch {
      case e: DMLException => Left(e)
    }
  }
}

class UserRepositoryMySQL extends MySQLAccessory(false) with UserRepository