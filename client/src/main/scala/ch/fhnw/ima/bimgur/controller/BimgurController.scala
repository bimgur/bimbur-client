package ch.fhnw.ima.bimgur.controller

import ch.fhnw.ima.bimgur.model.BimgurModel
import ch.fhnw.ima.bimgur.model.activiti.ProcessInstance
import ch.fhnw.ima.bimgur.service.RuntimeService
import diode._
import diode.data._
import diode.react.ReactConnector

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object BimgurController {

  val activitiRestUrl = "http://kermit:kermit@192.168.99.100:8080/activiti-rest/service"

  val runtimeService = RuntimeService(activitiRestUrl)

  // Actions

  case class UpdateAnalyses(potResult: Pot[Seq[ProcessInstance]] = Empty) extends PotAction[Seq[ProcessInstance], UpdateAnalyses] {
    override def next(newPotResult: Pot[Seq[ProcessInstance]]) = UpdateAnalyses(newPotResult)
  }

  // Action Handlers

  class AnalysesHandle(modelRW: ModelRW[BimgurModel, Pot[Seq[ProcessInstance]]]) extends ActionHandler(modelRW) {

    override def handle = {
      case action: UpdateAnalyses =>
        val updateF = action.effect {runtimeService.getProcessInstances}(identity)
        action.handleWith(this, updateF)(PotAction.handler())
    }

  }

  // Circuit

  object BimgurCircuit extends Circuit[BimgurModel] with ReactConnector[BimgurModel] {

    override protected def initialModel = BimgurModel(Empty)

    override protected val actionHandler = composeHandlers(
      new AnalysesHandle(zoomRW(_.analyses)((m, v) => m.copy(analyses = v)))
    )

    override def handleError(msg: String): Unit = {
      val name = BimgurController.getClass.getSimpleName
      println(s"[$name] Error: $msg")
    }

  }

}
