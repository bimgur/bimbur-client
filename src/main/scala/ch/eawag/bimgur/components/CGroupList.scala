package ch.eawag.bimgur.components

import ch.eawag.bimgur.model.{Group, GroupList}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom._
import org.scalajs.dom.ext.Ajax
import upickle.default._

import scala.concurrent.Future

object CGroupList {

  case class State(groups: Seq[Group])

  class Backend($: BackendScope[Unit, State]) {

    def loadGroups = Callback {
      val url = "http://kermit:kermit@localhost:8090/activiti-rest/service/identity/groups"
      import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

      val requestFuture: Future[XMLHttpRequest] = Ajax.get(url)
      requestFuture.onSuccess { case response =>
        val groups = read[GroupList](response.responseText).data
        println("Loaded new groups")
        $.modState(s => State(groups)).runNow()
      }
      requestFuture.onFailure { case error =>
        println(error)
      }
    }

    def render(S: State) = {

      def createItem(group: Group) = <.li(group.name)
      def renderGroups(groups: Seq[Group]) = <.ul(groups map createItem)

      <.div(
        <.h3("Activiti Groups"),
        renderGroups(S.groups),
        CChartD3(S.groups.map(_.name))
      )
    }
  }

  private val component = ReactComponentB[Unit]("CGroupList")
    .initialState(State(Nil))
    .renderBackend[Backend]
    .componentDidMount(_.backend.loadGroups)
    .build

  def apply() = component()

}
