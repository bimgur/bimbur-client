package ch.eawag.bimgur.view

import ch.eawag.bimgur.view.page.Page.PageId
import ch.eawag.bimgur.view.page.{GroupPage, UserPage}
import rx.core.{Obs, Rx}

import scalatags.JsDom.all._

class MainView(presentationModel: PresentationModel) {

  val userTab = li(role := "presentation")(a(href := "#users")("Users")).render
  val groupTab = li(role := "presentation")(a(href := "#groups")("Groups")).render

  val header = ul(`class` := "nav nav-tabs")(userTab, groupTab)

  val userPageClass = reactiveClass(UserPage.pageId)
  val groupPageClass = reactiveClass(GroupPage.pageId)

  def reactiveClass(pageId: PageId): Rx[String] =
    Rx {
      presentationModel.activePage() match {
        case Some(activePageId) if activePageId == pageId => "page active"
        case _ => "page"
      }
    }

  val userPage = div(id := "users")(UserPage.render).render
  val groupPage = div(id := "groups")(GroupPage.render).render

  val userPageClassObs = Obs(userPageClass) {
    userPage.setAttribute("class", userPageClass())
    userTab.setAttribute("class", userPageClass())
  }

  val groupPageClassObs = Obs(groupPageClass) {
    groupPage.setAttribute("class", groupPageClass())
    groupTab.setAttribute("class", groupPageClass())
  }

  val content = div(`class` := "container")(
    header,
    userPage,
    groupPage
  ).render

  def render = content

}