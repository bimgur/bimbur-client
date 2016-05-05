package ch.eawag.bimgur.view

import ch.eawag.bimgur.view.page.Page.PageId
import ch.eawag.bimgur.view.page.{GroupPage, UserPage}
import org.scalajs.dom
import rx.core.{Obs, Rx}

import scala.language.implicitConversions
import scala.scalajs.js
import scalatags.JsDom.all._

class MainView(presentationModel: PresentationModel) {

  val userPageActive = isPageActive(UserPage.pageId)
  val groupPageActive = isPageActive(GroupPage.pageId)

  /** Dynamic creation of CSS 'active' class */
  private def isPageActive(pageId: PageId): Rx[String] =
    Rx {
      presentationModel.activePage() match {
        case Some(activePageId) if activePageId == pageId => "active"
        case _ => ""
      }
    }

  val header = Rx {
    ul(`class` := "nav nav-tabs")(
      li(role := "presentation", `class` := userPageActive())(a(href := "#users")("Users")),
      li(role := "presentation", `class` := groupPageActive())(a(href := "#groups")("Groups")))
  }

  val userPage = Rx {
    div(id := "users", `class` := "page " + userPageActive())(UserPage.content)
  }

  val groupPage = Rx {
    div(id := "groups", `class` := "page " + groupPageActive())(GroupPage.content)
  }

  val root =
    div(`class` := "container")(
      header,
      userPage,
      groupPage
    )

  def content = root()

  /** https://www.lihaoyi.com/hands-on-scala-js/#Functional-ReactiveUIs */
  implicit def rxFrag[T](r: Rx[T])(implicit frag: T => Frag): Frag = {
    def rSafe: dom.Node = span(r()).render
    var last = rSafe
    Obs(r, skipInitial = true) {
      val newLast = rSafe
      js.Dynamic.global.last = last
      last.parentNode.replaceChild(newLast, last)
      last = newLast
    }
    last
  }

}