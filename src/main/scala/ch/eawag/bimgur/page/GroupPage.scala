package ch.eawag.bimgur.page

import ch.eawag.bimgur.model.GroupList
import org.scalajs.dom
import org.scalajs.dom._
import upickle.default._

import scala.concurrent.Future
import scalatags.JsDom.all._

object GroupPage extends Page {

  val Url = "http://kermit:kermit@localhost:8090/activiti-rest/service/identity/groups"

  val pageId = "groups"

  def render = {
    import dom.ext._

    import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    val loading = div("Loading Groups...").render
    val content = div(loading).render

    val requestFuture: Future[XMLHttpRequest] = Ajax.get(Url)
    requestFuture.onSuccess { case response =>
      val groups = read[GroupList](response.responseText).data
      val list = ul(for (group <- groups) yield li(group.name))
      content.removeChild(loading)
      content.appendChild(div(h1("Groups"), list).render)
    }
    requestFuture.onFailure { case error =>
      println(error)
    }
    content
  }

}
