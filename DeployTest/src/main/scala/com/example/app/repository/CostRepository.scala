package com.example.app.repository

import com.example.app.dao.{DMLException, DatabaseAccessory, MySQLAccessory, ResultSetException}
import com.example.app.domain.Cost
import com.example.app.repository.MoneyRepository.CreateUtilityCostsSQL

import java.sql.SQLException

trait CostRepository extends DatabaseAccessory{

  def read(cost: Cost): Either[SQLException, Map[String, Int]] ={

    val sql = s"SELECT * FROM utility_costs WHERE user_id = ${cost.userId} AND month = ${cost.month};"

    try {
      val rs = executeQuery(sql)
      try {
        rs.next()
      }
      catch {
        case e: SQLException => throw new ResultSetException(e)
      }
      val rent    = rs.getInt("rent")
      val card    = rs.getInt("card")
      val other   = rs.getInt("other")
      val friends = rs.getInt("friends")

      val map = Map(
        "user_id" -> cost.userId,
        "rent" -> rent,
        "card" -> card,
        "other" -> other,
        "friends" -> friends,
        "month" -> cost.month
      )

      Right(map)
    }
    catch {
      case e: DMLException => Left(e)
      case e1: ResultSetException => Left(e1)
    }
  }

  def create(cost: Cost): Either[DMLException, Int] ={

    val sqlFormat = "INSERT INTO utility_costs(user_id, month %s %s %s %s) " +
      s"VALUES(${cost.userId}, ${cost.month} %s %s %s %s)"

    def getSQLByOption(column: String, value: Option[Int]) = value match {
      case Some(x) => CreateUtilityCostsSQL(s",$column", s",$x")
      case None    => CreateUtilityCostsSQL("", "")
    }
    val rentSQL = getSQLByOption("rent", cost.rent)
    val cardSQL = getSQLByOption("card", cost.card)
    val otherSQL = getSQLByOption("other", cost.other)
    val friendsSQL = getSQLByOption("friends", cost.friends)

    val sql = sqlFormat.format(
      rentSQL.column,cardSQL.column,otherSQL.column,friendsSQL.column,
      rentSQL.value, cardSQL.value, otherSQL.value, friendsSQL.value)

    try {
      Right(executeUpdate(sql))
    }
    catch{
      case e: DMLException => Left(e)
    }
  }
}

class CostRepositoryMySQL extends MySQLAccessory(false) with CostRepository