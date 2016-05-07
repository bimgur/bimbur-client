package ch.eawag.bimgur.components

import ch.eawag.bimgur.model.{User, UserList}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom._
import org.scalajs.dom.ext.Ajax
import upickle.default._

import scala.concurrent.Future

object CUserList {

  case class State(users: Seq[User])

  class Backend($: BackendScope[Unit, State]) {

    def loadUsers = Callback {
      val url = "http://kermit:kermit@localhost:8090/activiti-rest/service/identity/users"
      import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
      val requestFuture: Future[XMLHttpRequest] = Ajax.get(url)
      requestFuture.onSuccess { case response =>
        val users = read[UserList](response.responseText).data
        println("Loaded new users")
        $.modState(s => State(users)).runNow()
      }
      requestFuture.onFailure { case error =>
        println(error)
      }
    }

    def render(S: State) = {

      def createItem(user: User) = <.li(user.firstName)
      def renderUsers(users: Seq[User]) = <.ul(users map createItem)

      <.div(
        <.h3("Activiti Users"),
        renderUsers(S.users),
        CChartD3(S.users.map(_.firstName))
      )
    }
  }

  private val component = ReactComponentB[Unit]("CUserList")
    .initialState(State(Nil))
    .renderBackend[Backend]
    .componentDidMount(_.backend.loadUsers)
    .build

  def apply() = component()

}
