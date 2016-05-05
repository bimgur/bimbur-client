package ch.eawag.bimgur.view.page

import ch.eawag.bimgur.model.{User, UserList}
import com.thoughtworks.binding.Binding.Vars
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.html.UList
import upickle.default._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object UserPage extends Page {

  val Url = "http://kermit:kermit@localhost:8090/activiti-rest/service/identity/users"

  val pageId = "users"

  val users = Vars.empty[User]

  val requestFuture = Ajax.get(Url)
  requestFuture.onSuccess { case response =>
    users.get ++= read[UserList](response.responseText).data
  }
  requestFuture.onFailure { case error =>
    println(error)
  }

  @dom
  def userList: Binding[UList] = {
    <ul>
      {for (user <- users) yield {
      <li>
        {user.firstName}
      </li>
    }}
    </ul>
  }

}
