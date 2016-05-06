package ch.eawag.bimgur

import ch.eawag.bimgur.App.Location.{GroupsLocation, UsersLocation}
import ch.eawag.bimgur.components.{CGroupList, CUserList}
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import org.scalajs.dom
import org.scalajs.dom.document

import scala.scalajs.js

object App extends js.JSApp {

  val baseUrl = BaseUrl(dom.window.location.href.takeWhile(_ != '#'))

  sealed abstract class Location(val link: String)

  object Location {

    object UsersLocation extends Location("users")

    object GroupsLocation extends Location("groups")

    def values = List[Location](UsersLocation, GroupsLocation)
  }

  val routerConfig: RouterConfig[Location] = RouterConfigDsl[Location].buildConfig { dsl =>
    import dsl._

    def filterRoute(s: Location): Rule = staticRoute("#/" + s.link, s) ~> renderR(ctl => {
      s match {
        case UsersLocation => CUserList()
        case GroupsLocation => CGroupList()
      }
    })

    val filterRoutes: Rule = Location.values.map(filterRoute).reduce(_ | _)

    filterRoutes.notFound(redirectToPage(Location.UsersLocation)(Redirect.Replace))
  }

  val router: ReactComponentU[Unit, Resolution[Location], Any, TopNode] =
    Router(baseUrl, routerConfig.logToConsole)()

  def main() = {
    ReactDOM.render(router, document.getElementById("root"))
  }

}
