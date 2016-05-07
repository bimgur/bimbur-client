package ch.eawag.bimgur.components

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB}
import org.scalajs.dom.EventTarget
import org.singlespaced.d3js.{Selection, d3}

object CChartD3 {

  case class State(selection: Option[Selection[EventTarget]])

  class Backend($: BackendScope[Seq[String], State]) {
    def render = <.div(^.id := "d3")
  }

  private val component = ReactComponentB[Seq[String]]("CChartD3")
    .initialState(State(None))
    .renderBackend[Backend]
    .componentDidMount(scope => Callback {
      val selection = d3.select("#d3").append("div")
      scope.modState(s => State(Some(selection))).runNow()
    })
    .shouldComponentUpdate(scope => {
      scope.nextState.selection match {
        case Some(selection) => selection.text("Reporting via D3: " + scope.nextProps.size + " items loaded")
        case _ =>
      }
      false
    })
    .build

  def apply(props: Seq[String]) = component(props)

}
