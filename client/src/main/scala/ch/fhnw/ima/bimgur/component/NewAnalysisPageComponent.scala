package ch.fhnw.ima.bimgur.component

import ch.fhnw.ima.bimgur.component.pages.Page
import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._

object NewAnalysisPageComponent {

  private val component = ReactComponentB[Unit](NewAnalysisPageComponent.getClass.getSimpleName)
    .render($ => <.div(
      <.h3(Page.NewAnalysisPage.pageTitle),
      TodoComponent()))
    .build

  def apply() = component()

}
