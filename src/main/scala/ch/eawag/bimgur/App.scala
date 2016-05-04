package ch.eawag.bimgur

import ch.eawag.bimgur.controller.PageController
import ch.eawag.bimgur.page.{GroupPage, Page, UserPage}
import org.scalajs.dom.{document, window}

import scala.scalajs.js.JSApp

object App extends JSApp {

  override def main() = {

    PageController.install(window, document)

    appendPage(UserPage)
    appendPage(GroupPage)
  }

  def appendPage(page: Page) = {
    val pageElement = document.getElementById(page.pageId)
    if (pageElement != null) {
      pageElement.appendChild(page.render)
    } else {
      println(s"HTML must contain an element with id '$page.pageId' (to dynamically attach contents)")
    }
  }

}
