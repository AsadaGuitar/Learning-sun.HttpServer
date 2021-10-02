package com.example.app.service

import com.example.app.domain.Cost
import com.example.app.session.{SessionException, SessionFactory}
import org.hibernate.HibernateException

import scala.collection.JavaConverters.asScalaBufferConverter


class CostService {

  def findOne(userId: Int): Either[SessionException, Option[Cost]] = {
    println(getClass.getName ++ ".findOne")
    //session
    val session = try {
      Right(SessionFactory.getSessionFactory.openSession)
    }
    catch {
      case e: HibernateException => Left(new SessionException(e))
    }

    for {
      s <- session
    } yield {
      val query = s.createQuery("From Cost", classOf[Cost])
      val costList = query.list().asScala
      costList.find(_.getUserId == userId)
    }
  }

  def insert(cost: Cost): Either[SessionException,Unit] ={
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
      s.save(cost)
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

