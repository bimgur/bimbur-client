package ch.eawag.bimgur.components

import ch.eawag.bimgur.model.User
import ch.eawag.bimgur.service.UserService
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object CUserList {

  private var users: Option[Seq[User]] = None

  case class Props(service: UserService)

  case class State(users: Seq[User])

  class Backend($: BackendScope[Props, State]) {

    def lazyLoadUsers(service: UserService): Callback = {
      users match {
        case Some(existingUsers) => $.setState(State(existingUsers))
        case None => updateUsers(service)
      }
    }

    def updateUsers(service: UserService) = Callback {
      for (newUsers <- service.getUsers) {
        users = Some(newUsers)
        $.setState(State(newUsers)).runNow()
      }
    }

    def render(p: Props, s: State) = {

      def createItem(user: User) = <.li(user.firstName)
      def renderUsers(users: Seq[User]) = <.ul(users map createItem)

      <.div(
        <.h3("Activiti Users"),
        renderUsers(s.users),
        <.button(^.onClick --> updateUsers(p.service), "Refresh"),
        CChartD3(s.users.map(_.firstName))
      )
    }
  }

  private val component = ReactComponentB[Props]("CUserList")
    .initialState(State(Nil))
    .renderBackend[Backend]
    .componentDidMount { scope =>
      scope.backend.lazyLoadUsers(scope.props.service)
    }
    .build

  def apply(userService: UserService) = component(Props(userService))

}
