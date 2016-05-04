package ch.eawag.bimgur.page

import ch.eawag.bimgur.model.{User, UserList}
import org.scalajs.dom
import org.scalajs.dom.XMLHttpRequest
import upickle.default._

import scala.concurrent.Future
import scalatags.JsDom.all._

class UserPage extends Page {

  val Url = "http://kermit:kermit@localhost:8090/activiti-rest/service/identity/users"

  def render = {
    import dom.ext._
    import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val content = div.render

    val requestFuture: Future[XMLHttpRequest] = Ajax.get(Url)
    requestFuture.onSuccess { case response =>
      val users = read[UserList](response.responseText).data
      val list = ul(for (user <- users) yield li(user.firstName))
      content.innerHTML = ""
      content.appendChild(list.render)
    }
    requestFuture.onFailure { case error =>
      println(error)
    }
    content.innerHTML = "Loading users..."
    content
  }

}
