package ch.fhnw.ima.bimgur.component

import ch.fhnw.ima.bimgur.component.pages.Page
import ch.fhnw.ima.bimgur.controller.BimgurController.UpdateMasterFormData
import ch.fhnw.ima.bimgur.model.activiti._
import diode.data.Pot
import diode.react.ModelProxy
import diode.react.ReactPot._
import japgolly.scalajs.react.{Callback, _}
import japgolly.scalajs.react.vdom.prefix_<^._

object NewAnalysisPageComponent {

  case class Props(proxy: ModelProxy[Pot[FormData]])

  class Backend($: BackendScope[Props, Unit]) {

    def lazyLoadMasterFormData(props: Props): Callback = {
      Callback.when(props.proxy().isEmpty)(refreshMasterFormData(props))
    }

    def refreshMasterFormData(props: Props) = props.proxy.dispatch(UpdateMasterFormData())

    def render(p: Props) = {
      <.div(
        <.h3(Page.NewAnalysisPage.pageTitle),
        p.proxy().renderFailed(ex => <.div("Loading failed (Console log for details)")),
        p.proxy().renderPending(_ > 500, _ => <.div("Loading...")),
        p.proxy().renderReady(FormDataComponent(_))
      )
    }

  }

  private val component = ReactComponentB[Props](getClass.getSimpleName)
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.lazyLoadMasterFormData(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[FormData]]) = component(Props(proxy))

}
