package ch.fhnw.ima.bimgur.components

import ch.fhnw.ima.bimgur.controller.BimgurController.UpdateGroups
import ch.fhnw.ima.bimgur.model.Group
import diode.data.Pot
import diode.react.ModelProxy
import diode.react.ReactPot._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

object CGroupList {

  case class Props(proxy: ModelProxy[Pot[Seq[Group]]])

  class Backend($: BackendScope[Props, Unit]) {

    def lazyLoadGroups(props: Props): Callback = {
      Callback.when(props.proxy().isEmpty)(refreshGroups(props))
    }

    def refreshGroups(props: Props) = props.proxy.dispatch(UpdateGroups())

    def render(p: Props) = {

      def createItem(group: Group) = <.li(group.name)
      def renderGroups(groups: Seq[Group]) = <.ul(groups map createItem)

      <.div(
        <.h3("Groups"),
        p.proxy().renderFailed(ex => <.div("Loading failed (Console log for details)")),
        p.proxy().renderPending(_ > 500, _ => <.div("Loading...")),
        p.proxy().renderReady(renderGroups),
        <.button(^.onClick --> refreshGroups(p), ^.role := "button", ^.className := "btn btn-default", "Refresh"),
        p.proxy().render(groups => CChartD3(groups.map(_.name)))
      )
    }
  }

  private val component = ReactComponentB[Props](getClass.getSimpleName)
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.lazyLoadGroups(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Seq[Group]]]) = component(Props(proxy))

}
