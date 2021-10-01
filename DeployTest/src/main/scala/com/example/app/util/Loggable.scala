package com.example.app.util

import org.apache.log4j.Logger

trait Loggable {

  protected val log: Logger

  protected def logging(classname: String, methodName: String) ={

  }


}
