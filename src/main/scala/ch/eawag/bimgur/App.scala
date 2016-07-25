package ch.eawag.bimgur

import ch.eawag.bimgur.App.Location.{GroupsLocation, UsersLocation}
import ch.eawag.bimgur.components.{CGroupList, CUserList}
import ch.eawag.bimgur.controller.BimgurController.BimgurCircuit
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import org.scalajs.dom.document

import scala.scalajs.js

object App extends js.JSApp {

  val baseUrl = BaseUrl(dom.window.location.href.takeWhile(_ != '#'))
  val activitiRestUrl = "http://kermit:kermit@localhost:8090/activiti-rest/service"

  // all supported URL hashes
  sealed abstract class Location(val link: String)

  object Location {

    object UsersLocation extends Location("users")

    object GroupsLocation extends Location("groups")

    def values = List[Location](UsersLocation, GroupsLocation)
  }

  // configures how URLs map to components
  val routerConfig: RouterConfig[Location] = RouterConfigDsl[Location].buildConfig { dsl =>
    import dsl._

    val userConnection = BimgurCircuit.connect(_.users)
    val groupConnection = BimgurCircuit.connect(_.groups)

    def filterRoute(s: Location): Rule = staticRoute("#/" + s.link, s) ~> renderR(ctl => {
      s match {
        case UsersLocation => userConnection(CUserList(_))
        case GroupsLocation => groupConnection(CGroupList(_))
      }
    })

    val filterRoutes: Rule = Location.values.map(filterRoute).reduce(_ | _)

    filterRoutes.notFound(redirectToPage(Location.UsersLocation)(Redirect.Replace))
  }.renderWith(layout)

  // base layout for all pages
  def layout(ctl: RouterCtl[Location], r: Resolution[Location]) = {

    def activeIf(loc: Location) = if (r.page == loc) "active" else ""
    val activeIfUsers = activeIf(UsersLocation)
    val activeIfGroups = activeIf(GroupsLocation)

    <.div(^.className := "container",
      <.ul(^.className := "nav nav-tabs",
        <.li(^.role := "presentation", ^.className := activeIfUsers, <.a(^.href := "#/users", "Users")),
        <.li(^.role := "presentation", ^.className := activeIfGroups, <.a(^.href := "#/groups", "Groups"))
      ),
      // currently active module is shown in this container
      <.div(^.className := "container", r.render())
    )
  }

  val router: ReactComponentU[Unit, Resolution[Location], Any, TopNode] =
    Router(baseUrl, routerConfig.logToConsole)()

  def main() = {
    ReactDOM.render(router, document.getElementById("root"))
  }

}
