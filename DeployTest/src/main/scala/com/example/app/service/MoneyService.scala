package com.example.app.service

import com.example.app.dao.{CommitException, ConnectionException, RollbackException, SavepointException}
import com.example.app.domain.Money
import com.example.app.repository.{MoneyRepository, MoneyRepositoryMySQL}

import java.util.{Calendar, Date}

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

trait MoneyService extends MoneyRepository{
  import MoneyService._

  def findAllMoney(userId: Int) = try {

    setConnection()

    readALLMoney(userId) match {
      case Right(rs) =>
        rs.next()
        val total = rs.getInt("total")
        val responseFormat = "Total Money = %d.\nMoney can use everyday = %d."
        Some(responseFormat.format(total, moneyToUseEveryDay(total)))

      case Left(e) =>
        println(s"Could not read.\n${e.getMessage}")
        None
    }
  }
  catch {
    case e: ConnectionException =>
      println(s"Could not to connect database.\n${e.getMessage}")
      None
  }

  def insert(money: Money): String ={

    try {

      setConnection()

      val sp = setSavepoint()

      create(money) match {
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

class MoneyServiceMySQL extends MoneyRepositoryMySQL with MoneyService
