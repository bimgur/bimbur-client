package ch.fhnw.ima.bimgur.components

import ch.fhnw.ima.bimgur.controller.BimgurController.UpdateUsers
import ch.fhnw.ima.bimgur.model.User
import diode.data.Pot
import diode.react.ModelProxy
import diode.react.ReactPot._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

object CUserList {

  case class Props(proxy: ModelProxy[Pot[Seq[User]]])

  class Backend($: BackendScope[Props, Unit]) {

    def lazyLoadUsers(props: Props): Callback = {
      Callback.when(props.proxy().isEmpty)(refreshUsers(props))
    }

    def refreshUsers(props: Props) = props.proxy.dispatch(UpdateUsers())

    def render(p: Props) = {

      def createItem(user: User) = <.li(user.firstName)
      def renderUsers(users: Seq[User]) = <.ul(users map createItem)

      <.div(
        <.h3("Users"),
        p.proxy().renderFailed(ex => <.div("Loading failed (Console log for details)")),
        p.proxy().renderPending(_ > 500, _ => <.div("Loading...")),
        p.proxy().renderReady(renderUsers),
        <.button(^.onClick --> refreshUsers(p), ^.role := "button", ^.className := "btn btn-default", "Refresh"),
        p.proxy().render(users => CChartD3(users.map(_.firstName)))
      )
    }
  }

  private val component = ReactComponentB[Props](getClass.getSimpleName)
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.lazyLoadUsers(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Seq[User]]]) = component(Props(proxy))

}
