package com.example.app.dao

import java.sql.{Connection, DriverManager, ResultSet, SQLException, Savepoint}

class ConnectionException(val exception: SQLException) extends SQLException
class CommitException(val exception: SQLException) extends SQLException
class SavepointException(val exception: SQLException) extends SQLException
class RollbackException(val exception: SQLException) extends SQLException
class CloseException(val exception: SQLException) extends SQLException
class DMLException(val exception: SQLException) extends SQLException

abstract class DatabaseAccessory extends Cloneable{

  protected val _url: String
  protected val _username: String
  protected val _password: String

  protected val _driverName: String
  protected val _autoCommit: Boolean
  protected var _conn: Connection = _

  @throws[ConnectionException]
  final def setConnection(): Unit = try {

    Class.forName(_driverName)
    _conn = DriverManager.getConnection(_url, _username, _password)
    _conn.setAutoCommit(_autoCommit)
  }
  catch {
    case e: SQLException => throw new ConnectionException(e)
  }

  @throws[CommitException]
  final def commit(): Unit = try{
    _conn.commit()
  }
  catch {
    case e: SQLException => throw new CommitException(e)
  }

  @throws[SavepointException]
  final def setSavepoint(): Savepoint = try{
    _conn.setSavepoint()
  }
  catch {
    case e: SQLException => throw new SavepointException(e)
  }

  @throws[RollbackException]
  final def rollBack(): Unit = try{
    _conn.rollback()
  }
  catch {
    case e: SQLException => throw new RollbackException(e)
  }

  @throws[RollbackException]
  final def rollBack(sp: Savepoint): Unit = try {
    _conn.rollback(sp)
  }
  catch {
    case e: SQLException => throw new RollbackException(e)
  }

  @throws[CloseException]
  final def close(): Unit = try{
    _conn.close()
  }
  catch {
    case e: SQLException => throw new CloseException(e)
  }

  @throws[DMLException]
  protected final def executeUpdate(sql: String): Int = try{
    val ps = _conn.prepareStatement(sql)
    ps.executeUpdate()
  }
  catch {
    case e: SQLException => throw new DMLException(e)
  }

  @throws[DMLException]
  protected final def executeQuery(sql: String): ResultSet = try{
    val ps = _conn.prepareStatement(sql)
    ps.executeQuery()
  }
  catch {
    case e: SQLException => throw new DMLException(e)
  }
}


class MySQLAccessory(autoCommit: Boolean) extends DatabaseAccessory {

  override protected val _url: String = "jdbc:mysql://localhost:3306/money_keep"
  override protected val _username: String = "root"
  override protected val _password: String = "Asd18894"

  override protected val _driverName: String = "com.mysql.jdbc.Driver"
  override protected val _autoCommit: Boolean = autoCommit
}
