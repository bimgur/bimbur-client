package ch.eawag.bimgur.controller

import ch.eawag.bimgur.view.PresentationModel
import ch.eawag.bimgur.view.page.{GroupPage, UserPage}
import org.scalajs.dom
import org.scalajs.dom.Window
import org.scalajs.dom.html.Document

class PageRouter(presentationModel: PresentationModel) {

  val knownPages = Seq(UserPage, GroupPage)

  def observePageNavigation(window: Window, document: Document) = {
    window.addEventListener("hashchange", { (event: dom.Event) =>
      val hash = window.location.hash
      navigateTo(hash.tail, document)
    }, false)
  }

  private def navigateTo(hash: String, document: Document) = {
    val page = knownPages.find(_.pageId == hash)
    page match {
      case Some(activePage) => presentationModel.activePage := Some(activePage.pageId)
      case _ =>
        println(s"Unknown location hash '#$hash'")
    }
  }

}
