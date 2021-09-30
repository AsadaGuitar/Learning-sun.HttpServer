package com.example.app.repository

import com.example.app.session.SessionFactory
import com.example.app.dao.{DatabaseAccessory, MySQLAccessory}
import com.example.app.domain.User
import org.hibernate.HibernateException

trait UserRepository extends DatabaseAccessory {

  protected def create(user: User): Either[HibernateException, Unit] = try {
    //session
    val session = SessionFactory.getSessionFactory.openSession()
    //insert
    session.beginTransaction()
    session.save(user)
    //commit
    def commit(): Unit = session.getTransaction.commit()
    Right(commit())
  }
  catch{
    case e: HibernateException => Left(e)
  }
}

class UserRepositoryMySQL extends MySQLAccessory(false) with UserRepository