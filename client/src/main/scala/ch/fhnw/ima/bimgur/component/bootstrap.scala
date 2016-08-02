package ch.fhnw.ima.bimgur.component

import ch.fhnw.ima.bimgur.style.GlobalStyles
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{ReactComponentB, ReactNode, _}

import scalacss.ScalaCssReact._

object bootstrap {

  val css = GlobalStyles

  object Button {

    case class Props(onClick: Callback)

    val component = ReactComponentB[Props](Button.getClass.getSimpleName)
      .renderPC((_, p, c) =>
        <.button(css.defaultButton, ^.onClick --> p.onClick, c)
      ).build

    def apply(onClick: Callback, children: ReactNode*) = component(Props(onClick), children: _*)

  }

}
