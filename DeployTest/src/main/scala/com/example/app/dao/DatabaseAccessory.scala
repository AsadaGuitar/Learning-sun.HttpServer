package com.example.app.dao

import java.sql.{Connection, DriverManager, ResultSet, Savepoint}

object DatabaseAccessory {

  @throws[java.sql.SQLException]
  def apply(url: String, username: String, password: String, autoCommit: Boolean): DatabaseAccessory ={
    new DatabaseAccessory(url, username, password, autoCommit)
  }

  @throws[java.sql.SQLException]
  def apply(url: String, username: String, password: String): DatabaseAccessory ={
    new DatabaseAccessory(url, username, password)
  }
}

final class DatabaseAccessory private(url: String, username: String, password: String, autoCommit: Boolean = true)
  extends Cloneable{

  require(url.nonEmpty && username.nonEmpty && password.nonEmpty)

  private val driver: String = "com.mysql.jdbc.Driver"
  Class.forName(driver)

  private val conn: Connection = DriverManager.getConnection(url, username, password)

  conn.setAutoCommit(autoCommit)

  @throws[java.sql.SQLException]
  def commit(): Unit = conn.commit()

  @throws[java.sql.SQLException]
  def setSavepoint(): Savepoint = conn.setSavepoint()

  @throws[java.sql.SQLException]
  def rollBack(): Unit = conn.rollback()

  @throws[java.sql.SQLException]
  def rollBack(sp: Savepoint): Unit = conn.rollback(sp)

  @throws[java.sql.SQLException]
  def close(): Unit = conn.close()

  @throws[java.sql.SQLException]
  def executeUpdate(sql: String): Int ={
    val ps = conn.prepareStatement(sql)
    ps.executeUpdate()
  }

  @throws[java.sql.SQLException]
  def executeQuery(sql: String): ResultSet ={
    val ps = conn.prepareStatement(sql)
    ps.executeQuery()
  }
}
