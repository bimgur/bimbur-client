package ch.fhnw.ima.bimgur.component

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{ReactComponentB, _}

/**
  * A simple placeholder component for contents that are not yet implemented.
  */
object TodoComponent {

  private val component = ReactComponentB[String](TodoComponent.getClass.getSimpleName)
    .render_P(msg => <.div(^.`class` := "alert alert-info", s"TODO: $msg"))
    .build

  def apply() = component("Implementation")

  def apply(msg: String) = component(msg)

}
