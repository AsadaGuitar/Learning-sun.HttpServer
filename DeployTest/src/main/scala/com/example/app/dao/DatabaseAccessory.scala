package com.example.app.dao

import java.sql.{Connection, DriverManager, ResultSet, SQLException, Savepoint}

class ConnectionException extends SQLException
class CommitException extends SQLException
class SavepointException extends SQLException
class RollbackException extends SQLException
class CloseException extends SQLException
class DMLException extends SQLException

abstract class DatabaseAccessory extends Cloneable{

  protected val _url: String
  protected val _username: String
  protected val _password: String

  protected val _driverName: String
  protected val _autoCommit: Boolean
  protected var _conn: Connection = _

  @throws[ConnectionException]
  final def setConnection(): Unit ={
    Class.forName(_driverName)
    _conn = DriverManager.getConnection(_url, _username, _password)
    _conn.setAutoCommit(_autoCommit)
  }

  @throws[CommitException]
  final def commit(): Unit = _conn.commit()

  @throws[SavepointException]
  final def setSavepoint(): Savepoint = _conn.setSavepoint()

  @throws[RollbackException]
  final def rollBack(): Unit = _conn.rollback()

  @throws[RollbackException]
  final def rollBack(sp: Savepoint): Unit = _conn.rollback(sp)

  @throws[CloseException]
  final def close(): Unit = _conn.close()

  @throws[DMLException]
  protected final def executeUpdate(sql: String): Int ={
    val ps = _conn.prepareStatement(sql)
    ps.executeUpdate()
  }

  @throws[DMLException]
  protected final def executeQuery(sql: String): ResultSet ={
    val ps = _conn.prepareStatement(sql)
    ps.executeQuery()
  }
}


class MySQLAccessory(autoCommit: Boolean) extends DatabaseAccessory {

  override protected val _url: String = "jdbc:mysql://localhost:3306/money_keep"
  override protected val _username: String = "root"
  override protected val _password: String = "Asd18894"

  override protected val _driverName: String = "com.mysql.jdbc.Driver"
  override protected val _autoCommit: Boolean = autoCommit
}
