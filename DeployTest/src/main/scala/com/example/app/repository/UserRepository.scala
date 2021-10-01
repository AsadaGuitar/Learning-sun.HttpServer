package com.example.app.repository

import com.example.app.session.SessionFactory
import com.example.app.dao.{DatabaseAccessory, MySQLAccessory}
import com.example.app.domain.User
import org.hibernate.{HibernateException, Session}

trait UserRepository extends DatabaseAccessory {

  var session: Session = _

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
  finally {
    session.close()
  }

}

class UserRepositoryMySQL extends MySQLAccessory(false) with UserRepository