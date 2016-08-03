package ch.fhnw.ima.bimgur
package controller

import cats.data.Xor
import ch.fhnw.ima.bimgur.model.BimgurModel
import ch.fhnw.ima.bimgur.model.activiti._
import ch.fhnw.ima.bimgur.service.{FormService, RepositoryService, RuntimeService}
import diode._
import diode.data._
import diode.react.ReactConnector

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object BimgurController {

  val activitiRestUrl = "http://kermit:kermit@192.168.99.100:8080/activiti-rest/service"

  val repositoryService = RepositoryService(activitiRestUrl)
  val runtimeService = RuntimeService(activitiRestUrl)
  val formService = FormService(activitiRestUrl)

  // Actions

  case class RefreshAnalyses(potResult: Pot[Seq[Analysis]] = Empty) extends PotAction[Seq[Analysis], RefreshAnalyses] {
    override def next(newPotResult: Pot[Seq[Analysis]]) = RefreshAnalyses(newPotResult)
  }

  case class RefreshMasterFormData(potResult: Pot[FormData] = Empty) extends PotAction[FormData, RefreshMasterFormData] {
    override def next(newPotResult: Pot[FormData]) = RefreshMasterFormData(newPotResult)
  }

  // Action Handlers

  class AnalysesHandler(modelRW: ModelRW[BimgurModel, Pot[Seq[Analysis]]]) extends ActionHandler(modelRW) {

    override def handle = {
      case action: RefreshAnalyses =>
        val effect = serviceCallEffect(runtimeService.getProcessInstances)(action)
        action.handleWith(this, effect)(PotAction.handler())
    }

  }

  class MasterFormDataHandler(modelRW: ModelRW[BimgurModel, Pot[FormData]]) extends ActionHandler(modelRW) {
    override def handle = {
      case action: RefreshMasterFormData =>

        val processDefinitionsResponse = repositoryService.getProcessDefinitions(MasterWorkflowKey)
        val formDataResponse = processDefinitionsResponse.flatMap {
          case Xor.Right(Seq(processDefinition)) => formService.getFormData(processDefinition.id)
          case _ => Future.successful(Xor.left(new Throwable(s"Retrieving unique master workflow with key '$MasterWorkflowKey' failed")))
        }

        val effect = serviceCallEffect(formDataResponse)(action)
        action.handleWith(this, effect)(PotAction.handler())
    }
  }

  // Wraps an async service call inside a diode effect, properly mapping Xor results

  def serviceCallEffect[A, P <: PotAction[A, P]](f: => Future[Xor[Throwable, A]])(action: PotAction[A, P])(implicit ec: ExecutionContext) =
    Effect(
      f.map {
        case Xor.Right(x) => action.ready(x)
        case Xor.Left(error) =>
          BimgurCircuit.handleError(action.getClass.getSimpleName + " failed: " + error)
          action.failed(error)
      }
    )

  // Circuit

  object BimgurCircuit extends Circuit[BimgurModel] with ReactConnector[BimgurModel] {

    override protected def initialModel = BimgurModel(Empty, Empty)

    override protected val actionHandler = composeHandlers(
      new MasterFormDataHandler(zoomRW(_.masterFormData)((m, v) => m.copy(masterFormData = v))),
      new AnalysesHandler(zoomRW(_.analyses)((m, v) => m.copy(analyses = v)))
    )

    override def handleError(msg: String): Unit = {
      val name = BimgurController.getClass.getSimpleName
      println(s"[$name] Error: $msg")
    }

  }

}
