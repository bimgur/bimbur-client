package ch.eawag.bimgur

import ch.eawag.bimgur.controller.PageController
import ch.eawag.bimgur.view.{MainView, PresentationModel}
import org.scalajs.dom.{document, window}

import scala.scalajs.js.JSApp

object App extends JSApp {

  override def main() = {
    val presentationModel = new PresentationModel
    val pageController = new PageController(presentationModel)

    pageController.observePageNavigation(window, document)

    val mainView = new MainView(presentationModel).render
    document.body.appendChild(mainView)
  }

}
