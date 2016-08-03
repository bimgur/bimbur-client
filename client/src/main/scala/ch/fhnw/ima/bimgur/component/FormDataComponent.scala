package ch.fhnw.ima.bimgur.component

import ch.fhnw.ima.bimgur.model.activiti._
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, ReactComponentB}

object FormDataComponent {

  case class Props(formData: FormData)

  case class State(formData: FormData)

  class Backend($: BackendScope[Props, State]) {

    def render(state: State) = {
      <.form(^.`class` := "form-horizontal",
        for (formProperty <- state.formData.formProperties)
          yield <.div(^.`class` := "form-group",
            <.label(^.`class` := "col-sm-2 control-label", formProperty.name),
            <.div(^.`class` := "col-sm-5",
              <.input.text(^.`class` := "form-control")
            )
          ),
        <.div(^.`class` := "form-group",
          <.div(^.`class` := "col-sm-offset-2 col-sm-5",
            <.button(^.`type` := "submit", ^.`class` := "btn btn-default", ^.disabled := "disabled", "Start")
          )
        )
      )
    }

  }

  private val component = ReactComponentB[Props](FormDataComponent.getClass.getSimpleName)
    .initialState_P(p => State(p.formData))
    .renderBackend[Backend]
    .build

  def apply(formData: FormData) = component(Props(formData))

}
