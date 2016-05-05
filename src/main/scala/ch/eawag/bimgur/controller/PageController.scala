package ch.eawag.bimgur.controller

import ch.eawag.bimgur.view.PresentationModel
import ch.eawag.bimgur.view.page.{GroupPage, UserPage}
import org.scalajs.dom
import org.scalajs.dom.Window
import org.scalajs.dom.html.Document

class PageController(presentationModel: PresentationModel) {

  val Pages = Seq(UserPage, GroupPage)

  def observePageNavigation(window: Window, document: Document) = {
    window.addEventListener("hashchange", { (event: dom.Event) =>
      val hash = window.location.hash
      navigateTo(hash.tail, document)
    }, false)
  }

  private def navigateTo(hash: String, document: Document) = {
    if (Pages.exists(_.pageId == hash)) {
      Pages.foreach { page =>
        if (page.pageId == hash) {
          presentationModel.activePage() = Some(page.pageId)
        }
      }
    } else {
      presentationModel.activePage() = None
      println(s"Unknown location hash '#$hash'")
    }
  }

}
