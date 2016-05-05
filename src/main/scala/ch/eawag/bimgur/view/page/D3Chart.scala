package ch.eawag.bimgur.view.page

import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.html.Div
import org.singlespaced.d3js.d3

object D3Chart {

  @dom
  def chart: Binding[Div] = {

    val div: Div = <div id="d3"></div>

    val svg = d3.
      select(div).
      append("svg").
      attr("width", "100%").
      attr("height", 400)

    svg
      .append("text")
      .text(s"Hello from D3, it's ${System.currentTimeMillis()}")
      .attr("y", 50)

    <div>
      {div}
    </div>
  }

}
