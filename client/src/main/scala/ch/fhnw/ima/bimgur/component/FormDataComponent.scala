package ch.fhnw.ima.bimgur.component

import ch.fhnw.ima.bimgur.controller.BimgurController.StartAnalysis
import ch.fhnw.ima.bimgur.model.activiti._
import diode.data.Pot
import diode.react.ModelProxy
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB}

object FormDataComponent {

  case class Props(proxy: ModelProxy[Pot[FormData]])

  case class State(formData: FormData)

  class Backend($: BackendScope[Props, State]) {

    def handleSubmit: Callback =
      $.props >>= { props =>
        val id: ProcessDefinitionId = props.proxy.value.head.processDefinitionId
        val properties = Seq(StartFormProperty("analysis-name", "Test"))
        val data = StartProcessFormData(id, properties)
        props.proxy.dispatch(StartAnalysis(data))
      }

    def render(state: State) = {
      <.form(^.`class` := "form-horizontal", ^.onSubmit --> handleSubmit,
        for (formProperty <- state.formData.formProperties)
          yield <.div(^.`class` := "form-group",
            <.label(^.`class` := "col-sm-2 control-label", formProperty.name),
            <.div(^.`class` := "col-sm-5",
              <.input.text(^.`class` := "form-control")
            )
          ),
        <.div(^.`class` := "form-group",
          <.div(^.`class` := "col-sm-offset-2 col-sm-5",
            <.button(^.`type` := "submit", ^.`class` := "btn btn-default", "Start")
          )
        )
      )
    }

  }

  private val component = ReactComponentB[Props](FormDataComponent.getClass.getSimpleName)
    .initialState_P(p => State(p.proxy.value.head))
    .renderBackend[Backend]
    .build

  def apply(proxy: ModelProxy[Pot[FormData]]) = component(Props(proxy))

}
