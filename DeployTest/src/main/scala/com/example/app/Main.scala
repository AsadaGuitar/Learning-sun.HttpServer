package com.example.app

import com.example.app.api.APIServer

object Main extends App {

  try {
    APIServer.startServer()
  }
  catch {
    case e: java.io.IOException => println(e.getMessage)
  }
//  val log = Logger.getLogger(this.getClass.getName)
//  log.info("HELLO")
//
//  // session
//  val session: Session = SessionFactory.getSessionFactory.openSession
//
//  // insert
//  session.beginTransaction
//  val user: User = User(0, "this is new test", "0000")
//  val money: Money = Money(9, 100000, 1000)
//  session.save(user)
//  session.save(money)
//  session.getTransaction.commit()
//
//  // select
//  val userQuery = session.createQuery("From User", classOf[User])
//  val moneyQuery = session.createQuery("From Money", classOf[Money])
//
//  val userResults = userQuery.list()
//  val moneyResults = moneyQuery.list()
//
//  userResults.stream().forEach(x => println(x.getId, x.getName, x.getPass))
//  moneyResults.stream().forEach(x => println(x.getUserId, x.getSaving, x.getWallet))
//
//  session.close()
}
