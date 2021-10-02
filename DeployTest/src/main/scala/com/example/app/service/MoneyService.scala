package com.example.app.service

import com.example.app.domain.Money
import com.example.app.session.{SessionException, SessionFactory}
import org.hibernate.HibernateException

import java.util.{Calendar, Date}
import scala.collection.JavaConverters._

object MoneyService {

  private final val PAYDAY = 15

  private def differenceDays(date1: Date, date2: Date) ={
    val dateTime1 = date1.getTime
    val dateTime2 = date2.getTime
    val oneDateTime = 1000 * 60 * 60 * 24
    val diffDays = (dateTime1 - dateTime2) / oneDateTime
    diffDays.toInt
  }

  private val moneyToUseEveryDay: Int => Int = (total: Int) => {
    val date = new Date()
    val cld = Calendar.getInstance()
    val day = cld.get(Calendar.DATE)
    val diffPay = PAYDAY - day

    cld.setTime(date)
    if (PAYDAY <= day){
      cld.add(Calendar.MONTH,1)
    }
    cld.add(Calendar.DATE, diffPay)
    val payDate = cld.getTime
    val diffDays = differenceDays(payDate, date)

    total / diffDays
  }
}

class MoneyService {

  def selectAll(): Either[SessionException, Seq[Money]] = {

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
      val query = s.createQuery("From Money", classOf[Money])
      val javaList: java.util.List[Money] = query.list()
      val scalaList = javaList.asScala
      scalaList.toSeq
    }
  }

  def insert(money: Money): Either[SessionException, Unit] ={

    //session
    lazy val session = try {
      Right(SessionFactory.getSessionFactory.openSession)
    }
    catch {
      case e: HibernateException => Left(new SessionException(e))
    }

    val result = for{
      s <- session
    } yield  {
      //insert
      s.beginTransaction()
      s.save(money)
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

