package com.example.app.service

import com.example.app.domain.User
import com.example.app.session.{SessionException, SessionFactory}
import org.hibernate.HibernateException

class UsersService {

  def insert(user: User): Either[SessionException, Unit] = {

    //session
    val session = try {
      Right(SessionFactory.getSessionFactory.openSession)
    }
    catch {
      case e: HibernateException => Left(new SessionException(e))
    }

    val result = for {
      s <- session
    } yield {
      //insert
      s.beginTransaction()
      s.save(user)
      //commit
      s.getTransaction.commit()
      //close
      try {
        Right(s.close())
      }
      catch {
        case e: java.io.IOException => Left(new SessionException(e))
      }
    }

    result.flatten
  }
}

