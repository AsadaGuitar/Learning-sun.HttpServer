package com.example.app.domain

class User (_id: Option[Int], _name: String, _pass: String){

  def this(name: String, pass: String) = this(None, name, pass)
  def this(id: Int, name: String, pass: String) = this(Some(id), name, pass)

  def id: Option[Int] = _id
  def name: String = _name
  def pass: String = _pass
}
