package com.example.app.session

import java.io.IOException

@SerialVersionUID(1L)
class SessionException(e: Throwable) extends IOException {

  override def getCause = e

  override def getMessage = getCause.getMessage
}
