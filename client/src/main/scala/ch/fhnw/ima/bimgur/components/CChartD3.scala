package ch.fhnw.ima.bimgur.components

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB}
import org.scalajs.dom.EventTarget
import org.singlespaced.d3js.Ops._
import org.singlespaced.d3js.{Selection, d3}

import scala.scalajs.js.JSConverters._

object CChartD3 {

  case class State(svg: Option[Selection[EventTarget]])

  class Backend($: BackendScope[Seq[String], State]) {
    def render = <.div(^.id := "d3")
  }

  private val component = ReactComponentB[Seq[String]]("CChartD3")
    .initialState(State(None))
    .renderBackend[Backend]
    .componentDidMount(scope => {
      val svg = d3.select("#d3").append("div").append("svg")
      scope.modState(s => State(Some(svg)))
    })
    .componentWillUnmount(scope => Callback {
      scope.state.svg match {
        case Some(svg) => svg.select("svg").remove()
        case _ =>
      }
    })
    .shouldComponentUpdate(scope => {
      scope.nextState.svg match {
        case Some(svg) =>
          val circles = svg.selectAll("circle").data(scope.nextProps.toJSArray)

          val color = d3.scale.category10()

          circles.enter().append("circle")
            .attr("cy", "20")
            .attr("cx", (d: String, i: Int) => i * 25 + 10)
            .attr("r", "10")

          circles.exit().remove()

          circles.style("fill", (d: String) => color(d))
        case _ =>
      }
      false
    })
    .build

  def apply(props: Seq[String]) = component(props)

}
