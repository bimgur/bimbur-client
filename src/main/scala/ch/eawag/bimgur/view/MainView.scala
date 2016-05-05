package ch.eawag.bimgur.view

import ch.eawag.bimgur.view.page.Page.PageId
import ch.eawag.bimgur.view.page.{ D3Chart, GroupPage, Page, UserPage }
import com.thoughtworks.binding.{ Binding, dom }
import org.scalajs.dom.html.Div

import scala.language.implicitConversions

class MainView(pm: PresentationModel) {

  @dom
  def root: Binding[Div] = {

    val activePage = pm.activePage.each
    val activeIfUserPage = activeIfPage(activePage, UserPage)
    val activeIfGroupPage = activeIfPage(activePage, GroupPage)

    <div class="container">
      <ul class="nav nav-tabs">
        <li class={ activeIfUserPage }>
          <a href="#users">Users</a>
        </li>
        <li class={ activeIfGroupPage }>
          <a href="#groups">Groups</a>
        </li>
      </ul>
      <div id="users" class={ s"page $activeIfUserPage" }>
        { UserPage.userList.each }
      </div>
      <div id="groups" class={ s"page $activeIfGroupPage" }>
        { GroupPage.groupList.each }
      </div>
      { D3Chart.chart.each }
    </div>
  }

  private def activeIfPage(activePage: Option[PageId], page: Page): String = {
    activePage match {
      case Some(id) if id == page.pageId => "active"
      case _                             => ""
    }
  }

}