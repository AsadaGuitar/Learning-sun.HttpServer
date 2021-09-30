package com.example.app.session

import org.hibernate.cfg.Configuration

object SessionFactory {

  private val sessionFactory = buildSessionFactory

  private def buildSessionFactory = try {
    new Configuration().configure.buildSessionFactory
  }
  catch {
    case e: Throwable =>
      println("build SessionFactory failed.", e)
      throw new ExceptionInInitializerError(e)
  }

  def getSessionFactory = sessionFactory

  def close(): Unit = {
    getSessionFactory.close()
  }
}
