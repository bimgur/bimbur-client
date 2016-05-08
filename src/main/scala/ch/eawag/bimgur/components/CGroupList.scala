package ch.eawag.bimgur.components

import ch.eawag.bimgur.model.Group
import ch.eawag.bimgur.service.GroupService
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object CGroupList {

  private var groups: Option[Seq[Group]] = None

  case class Props(service: GroupService)

  case class State(groups: Seq[Group])

  class Backend($: BackendScope[Props, State]) {

    def lazyLoadGroups(service: GroupService): Callback = {
      groups match {
        case Some(existingGroups) => $.setState(State(existingGroups))
        case None => updateGroups(service)
      }
    }

    def updateGroups(service: GroupService) = Callback {
      for (newGroups <- service.getGroups) {
        groups = Some(newGroups)
        $.setState(State(newGroups)).runNow()
      }
    }

    def render(p: Props, s: State) = {

      def createItem(group: Group) = <.li(group.name)
      def renderGroups(groups: Seq[Group]) = <.ul(groups map createItem)

      <.div(
        <.h3("Activiti Groups"),
        renderGroups(s.groups),
        <.button(^.onClick --> updateGroups(p.service), "Refresh"),
        CChartD3(s.groups.map(_.name))
      )
    }
  }

  private val component = ReactComponentB[Props]("CGroupList")
    .initialState(State(Nil))
    .renderBackend[Backend]
    .componentDidMount { scope =>
      scope.backend.lazyLoadGroups(scope.props.service)
    }
    .build

  def apply(groupService: GroupService) = component(Props(groupService))

}
