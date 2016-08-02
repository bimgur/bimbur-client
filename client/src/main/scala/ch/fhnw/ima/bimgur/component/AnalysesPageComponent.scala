package ch.fhnw.ima.bimgur
package component

import ch.fhnw.ima.bimgur.component.bootstrap.Button
import ch.fhnw.ima.bimgur.component.pages.Page
import ch.fhnw.ima.bimgur.controller.BimgurController.UpdateAnalyses
import ch.fhnw.ima.bimgur.model.activiti.{Analysis, Variable}
import ch.fhnw.ima.bimgur.style.GlobalStyles
import diode.data.Pot
import diode.react.ModelProxy
import diode.react.ReactPot._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._

object AnalysesPageComponent {

  case class Props(proxy: ModelProxy[Pot[Seq[Analysis]]])

  class Backend($: BackendScope[Props, Unit]) {

    def lazyLoadAnalyses(props: Props): Callback = {
      Callback.when(props.proxy().isEmpty)(refreshAnalyses(props))
    }

    def refreshAnalyses(props: Props) = props.proxy.dispatch(UpdateAnalyses())

    def render(p: Props) = {

      val css = GlobalStyles

      def renderAnalysis(analysis: Seq[Analysis]) =
        <.table(css.table,
          <.thead(renderHeaderRow),
          <.tbody(analysis map renderAnalysisRow)
        )

      def renderHeaderRow = <.tr(<.th("Id"), <.th("Variables"))

      def renderAnalysisRow(analysis: Analysis) =
        <.tr(
          <.td(analysis.id),
          <.td(renderVariables(analysis.variables))
        )

      def renderVariables(variables: Seq[Variable]) = {
        val nameAndValueSeq = variables map (v => s"${v.name}: ${v.value.value}")
        nameAndValueSeq.mkString(", ")
      }

      <.div(
        <.h3(Page.AnalysesPage.pageTitle),
        p.proxy().renderFailed(ex => <.div("Loading failed (Console log for details)")),
        p.proxy().renderPending(_ > 500, _ => <.div("Loading...")),
        p.proxy().renderReady(renderAnalysis),
        Button(refreshAnalyses(p), "Refresh")
      )
    }
  }

  private val component = ReactComponentB[Props](getClass.getSimpleName)
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.lazyLoadAnalyses(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Seq[Analysis]]]) = component(Props(proxy))

}
