package com.example.app

import com.example.app.domain.{Money, User}
import com.example.app.session.SessionFactory
import org.apache.log4j.Logger
import org.hibernate.Session

object Main extends App {
//
//  for {
//    s <- APIServer.startServer()
//  } yield s
  val log = Logger.getLogger(this.getClass.getName)
  log.info("HELLO")

  // session
  val session: Session = SessionFactory.getSessionFactory.openSession

  // insert
  session.beginTransaction
  val user: User = User(0, "this is new test", "0000")
  val money: Money = Money(1, 100000, 1000)
  session.save(user)
  session.save(money)
  session.getTransaction.commit()

  // select
  val userQuery = session.createQuery("From User", classOf[User])
  val moneyQuery = session.createQuery("From Money", classOf[Money])

  val userResults = userQuery.list()
  val moneyResults = moneyQuery.list()

  userResults.stream().forEach(x => println(x.getId, x.getName, x.getPass))
  moneyResults.stream().forEach(x => println(x.getUserId, x.getSaving, x.getWallet))

  session.close()
}
