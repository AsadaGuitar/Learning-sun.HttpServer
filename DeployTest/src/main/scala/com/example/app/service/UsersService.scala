package com.example.app.service

import com.example.app.dao.{CommitException, ConnectionException, RollbackException, SavepointException}
import com.example.app.domain.User
import com.example.app.repository.{UserRepository, UserRepositoryMySQL}


trait UsersService extends UserRepository {

  def insert(user: User): String ={
    try {

      setConnection()

      val sp = setSavepoint()

      create(user) match {
        case Right(_) =>
        case Left(e) => rollBack(sp)
          return s"Could not create 'users'.\n${e.getMessage}"
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

    "Success to commit."
  }
}

class UserServiceMySQL extends UserRepositoryMySQL with UsersService