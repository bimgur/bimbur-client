package ch.fhnw.ima.bimgur

import ch.fhnw.ima.bimgur.component.pages.Page
import ch.fhnw.ima.bimgur.component.pages.Page._
import ch.fhnw.ima.bimgur.component.{AnalysesPageComponent, NewAnalysisPageComponent}
import ch.fhnw.ima.bimgur.controller.BimgurController.BimgurCircuit
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import org.scalajs.dom.document

import scala.scalajs.js

object App extends js.JSApp {

  val baseUrl = BaseUrl(dom.window.location.href.takeWhile(_ != '#'))

  // configures how URLs map to components
  val routerConfig: RouterConfig[Page] = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    // re-usable connections to controller
    val analysesModelConnection = BimgurCircuit.connect(_.analyses)

    // defines how hash-prefixed locations are mapped to a rendered component
    def filterRoute(page: Page): Rule = staticRoute("#/" + page.hashLink, page) ~> renderR(ctl => {
      page match {
        case AnalysesPage => analysesModelConnection(AnalysesPageComponent(_))
        case NewAnalysisPage => NewAnalysisPageComponent()
      }
    })

    val filterRoutes: Rule = Page.pages.map(filterRoute).reduce(_ | _)

    filterRoutes.notFound(redirectToPage(AnalysesPage)(Redirect.Replace))
  }.renderWith(layout)

  // base layout for all pages
  def layout(ctl: RouterCtl[Page], r: Resolution[Page]) = {

    def activeIf(loc: Page) = if (r.page == loc) "active" else ""
    <.div(^.className := "container",
      <.ul(^.className := "nav nav-tabs",
        Page.pages.map { page =>
          <.li(^.role := "presentation", ^.className := activeIf(page), <.a(^.href := s"#/${page.hashLink}", page.tabName))
        }
      ),
      // currently active module is shown in this container
      <.div(^.className := "container", r.render())
    )
  }

  def main() = {
    // create the router component (which knows what to render based on defined rules > see routerConfig above)
    val router = Router(baseUrl, routerConfig.logToConsole)()
    // actually render the router component
    ReactDOM.render(router, document.getElementById("root"))
  }

}
