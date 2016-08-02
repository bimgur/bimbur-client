package ch.fhnw.ima.bimgur.component

import ch.fhnw.ima.bimgur.style.GlobalStyles
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{ReactComponentB, _}

import scalacss.ScalaCssReact._

/**
  * A simple placeholder component for contents that are not yet implemented.
  */
object TodoComponent {

  val css = GlobalStyles

  private val component = ReactComponentB[String](TodoComponent.getClass.getSimpleName)
    .render_P(msg => <.div(css.infoBox, s"TODO: $msg"))
    .build

  def apply() = component("Implementation")

  def apply(msg: String) = component(msg)

}
