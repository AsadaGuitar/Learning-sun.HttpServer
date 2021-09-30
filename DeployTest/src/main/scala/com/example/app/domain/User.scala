package com.example.app.domain

import javax.persistence._

object User {
  def apply(id: Int, name: String, pass: String): User ={
    val user = new User
    user.setId(id)
    user.setName(name)
    user.setPass(pass)
    user
  }
}

@Entity
@Table(name = "users")
class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private var id: Int = _

  @Column(name = "name")
  private var name: String = _

  @Column(name = "password")
  private var pass: String = _

  def setId(id: Int): Unit = this.id = id
  def setName(name: String): Unit = this.name = name
  def setPass(pass: String): Unit = this.pass = pass

  def getId: Int = id
  def getName: String = name
  def getPass: String = pass
}
