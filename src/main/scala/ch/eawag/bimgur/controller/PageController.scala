package ch.eawag.bimgur.controller

import ch.eawag.bimgur.page.{GroupPage, UserPage}
import org.scalajs.dom
import org.scalajs.dom.Window
import org.scalajs.dom.html.Document

object PageController {

  val Pages = Seq(UserPage, GroupPage)

  def install(window: Window, document: Document) = {
    window.addEventListener("hashchange", { (event: dom.Event) =>
      val hash = window.location.hash
      navigateTo(hash.tail, document)
    }, false)
  }

  private def navigateTo(hash: String, document: Document) = {

    if (Pages.exists(_.pageId == hash)) {
      Pages.foreach { page =>
        val element = document.getElementById(page.pageId)
        if (page.pageId == hash) {
          element.setAttribute("style", "display:block")
        } else {
          element.setAttribute("style", "display:none")
        }
      }
    } else {
      println(s"Unknown location hash '#$hash'")
    }

  }

}
