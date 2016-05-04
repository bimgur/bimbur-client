package ch.eawag.bimgur.model

case class UserList(data: Seq[User])

case class User(firstName: String)