package ch.fhnw.ima.bimgur.service

import ch.fhnw.ima.bimgur.model.{User, UserList}
import org.scalajs.dom.ext.Ajax
import upickle.default._

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

case class UserService(activitiRestUrl: String) {

  val url = activitiRestUrl + "/identity/users"

  def getUsers: Future[Seq[User]] = {
    val requestFuture = Ajax.get(url)
    requestFuture.map(response => read[UserList](response.responseText).data)
  }

}