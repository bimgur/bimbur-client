package ch.fhnw.ima.bimgur
package component

import ch.fhnw.ima.bimgur.controller.BimgurController.StartAnalysis
import ch.fhnw.ima.bimgur.model.activiti._
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

object FormDataComponent {

  case class Props(proxy: ModelProxy[FormData])

  case class State(formValues: Map[FormPropertyId, String])

  class Backend($: BackendScope[Props, State]) {

    def handleSubmit(p: Props, s: State): Callback = {
      val processDefinitionId = p.proxy().processDefinitionId
      val formProperties = (for ((id, formValue) <- s.formValues) yield PersistableFormPropertyValue(id, formValue)).toSeq
      val data = StartProcessFormData(processDefinitionId, formProperties)
      p.proxy.dispatch(StartAnalysis(data))
    }

    def updateFormPropertyValue(id: FormPropertyId)(e: ReactEventI) = {
      val newValue = e.target.value
      $.modState(s => s.copy(formValues = s.formValues.updated(id, newValue)))
    }

    def render(p: Props, s: State) = {
      <.form(^.`class` := "form-horizontal", ^.onSubmit --> handleSubmit(p, s),
        for (formProperty <- p.proxy().formProperties)
          yield <.div(^.`class` := "form-group",
            <.label(^.`class` := "col-sm-2 control-label", formProperty.name),
            <.div(^.`class` := "col-sm-5",
              <.input.text(^.`class` := "form-control", ^.value := s.formValues(formProperty.id), ^.onChange ==> updateFormPropertyValue(formProperty.id))
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

  private def createFormValues(formProperties: Seq[FormProperty]): Map[FormPropertyId, String] = {
    (for (formProperty <- formProperties)
      yield (formProperty.id, formProperty.value.map(String.valueOf(_)).getOrElse(""))).toMap
  }

  private val component = ReactComponentB[Props](FormDataComponent.getClass.getSimpleName)
    .initialState_P(p => State(createFormValues(p.proxy().formProperties)))
    .renderBackend[Backend]
    .build

  def apply(proxy: ModelProxy[FormData]) = component(Props(proxy))

}
