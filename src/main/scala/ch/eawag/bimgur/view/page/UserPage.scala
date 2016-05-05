package ch.eawag.bimgur.view.page

import ch.eawag.bimgur.model.UserList
import org.scalajs.dom
import org.scalajs.dom.XMLHttpRequest
import upickle.default._

import scala.concurrent.Future
import scalatags.JsDom.all._

object UserPage extends Page {

  val Url = "http://kermit:kermit@localhost:8090/activiti-rest/service/identity/users"

  val pageId = "users"

  def content = {
    import dom.ext._

    import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val loading = div("Loading Users...").render
    val content = div(loading).render

    val requestFuture: Future[XMLHttpRequest] = Ajax.get(Url)
    requestFuture.onSuccess { case response =>
      val users = read[UserList](response.responseText).data
      val list = ul(for (user <- users) yield li(user.firstName))
      content.removeChild(loading)
      content.appendChild(div(h1("Users"), list).render)
    }
    requestFuture.onFailure { case error =>
      println(error)
    }
    content
  }

}
