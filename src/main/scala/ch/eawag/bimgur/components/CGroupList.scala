package ch.eawag.bimgur.components

import ch.eawag.bimgur.model.group.{Group, GroupList}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom._
import org.scalajs.dom.ext.Ajax
import upickle.default._

import scala.concurrent.Future

object CGroupList {

  case class State(groups: Seq[Group])

  class Backend($: BackendScope[Unit, State]) {

    def fetchGroups(e: ReactEventI) = {
      val Url = "http://kermit:kermit@localhost:8090/activiti-rest/service/identity/groups"
      import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

      val requestFuture: Future[XMLHttpRequest] = Ajax.get(Url)
      requestFuture.onSuccess { case response =>
        val groups = read[GroupList](response.responseText).data
        println("Loaded new Groups")
        $.modState(s => State(groups)).runNow()
      }
      requestFuture.onFailure { case error =>
        println(error)
      }
      $.modState(s => State(Nil))
    }

    def render(S: State) = {

      def createItem(group: Group) = <.li(group.name)

      def renderGroups(groups: Seq[Group]) = <.ul(groups map createItem)

      <.div(
        <.h3("Activiti Groups"),
        renderGroups(S.groups),
        <.button("Refresh", ^.onClick ==> fetchGroups)
      )
    }
  }

  private val component = ReactComponentB[Unit]("CGroupList")
    .initialState(State(Nil))
    .renderBackend[Backend]
    .build

  def apply() = component()

}
