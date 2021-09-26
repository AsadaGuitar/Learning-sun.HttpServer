package com.example.app.service

import com.example.app.dao.{CommitException, ConnectionException, RollbackException, SavepointException}
import com.example.app.domain.Cost
import com.example.app.repository.{CostRepository, CostRepositoryMySQL}


trait CostService extends CostRepository{

  def findOne(cost: Cost): Either[String, Map[String, Int]] = try {

    setConnection()
    val sp = setSavepoint()
    val map = read(cost)

    map match {
      case Right(x) => Right(x)
      case Left(e) =>
        rollBack(sp)
        Left(s"Could not read.\n${e.getMessage}")
    }
  }
  catch {
    case e1: ConnectionException => Left(s"Could not to connect database.\n${e1.getMessage}")
    case e2: SavepointException => Left(s"Could not to get 'Savepoint'.\n${e2.getMessage}")
    case e3: RollbackException => Left(s"Could not to rollback\n${e3.getMessage}")
    case e4: CommitException => Left(s"Could not to commit\n${e4.getMessage}")
  }
  finally {
    close()
  }

  def insert(cost: Cost): String ={

    try {

      setConnection()

      val sp = setSavepoint()

      create(cost) match {
        case Right(_) => println("success")
        case Left(e) =>
          println(s"Could not create.\n${e.getMessage}")
          rollBack(sp)
      }

      commit()
    }
    catch {
      case e1: ConnectionException => return s"Could not to connect database.\n${e1.getMessage}"
      case e2: SavepointException => return s"Could not to get 'Savepoint'.\n${e2.getMessage}"
      case e3: RollbackException => return s"Could not to rollback\n${e3.getMessage}"
      case e4: CommitException => return s"Could not to commit\n${e4.getMessage}"
    }
    finally {
      close()
    }

    "success to commit."
  }

}

class CostServiceMySQL extends CostRepositoryMySQL with CostService