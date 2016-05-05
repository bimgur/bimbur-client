package ch.eawag.bimgur

import ch.eawag.bimgur.controller.PageRouter
import ch.eawag.bimgur.view.{MainView, PresentationModel}
import com.thoughtworks.binding.dom
import org.scalajs.dom.{document, window}

import scala.scalajs.js.JSApp

object App extends JSApp {

  override def main() = {
    val presentationModel = new PresentationModel
    val router = new PageRouter(presentationModel)

    router.observePageNavigation(window, document)

    val mainView = new MainView(presentationModel).root
    dom.render(document.getElementById("root"), mainView)
  }

}
