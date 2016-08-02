package ch.fhnw.ima.bimgur
package controller

import cats.data.Xor
import ch.fhnw.ima.bimgur.model.BimgurModel
import ch.fhnw.ima.bimgur.model.activiti.Analysis
import ch.fhnw.ima.bimgur.service.RuntimeService
import diode._
import diode.data._
import diode.react.ReactConnector
import io.circe

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object BimgurController {

  val activitiRestUrl = "http://kermit:kermit@192.168.99.100:8080/activiti-rest/service"

  val runtimeService = RuntimeService(activitiRestUrl)

  // Actions

  case class UpdateAnalyses(potResult: Pot[Seq[Analysis]] = Empty) extends PotAction[Seq[Analysis], UpdateAnalyses] {
    override def next(newPotResult: Pot[Seq[Analysis]]) = UpdateAnalyses(newPotResult)
  }

  // Action Handlers

  class AnalysesHandler(modelRW: ModelRW[BimgurModel, Pot[Seq[Analysis]]]) extends ActionHandler(modelRW) {

    override def handle = {
      case action: UpdateAnalyses =>
        val effect = serviceCallEffect(runtimeService.getProcessInstances)(action)
        action.handleWith(this, effect)(PotAction.handler())
    }

  }

  // Wraps an async service call inside a diode effect, properly mapping Xor results

  def serviceCallEffect[A, P <: PotAction[A, P]](f: => Future[Xor[circe.Error, A]])(action: PotAction[A, P])(implicit ec: ExecutionContext) =
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

    override protected def initialModel = BimgurModel(Empty)

    override protected val actionHandler = composeHandlers(
      new AnalysesHandler(zoomRW(_.analyses)((m, v) => m.copy(analyses = v)))
    )

    override def handleError(msg: String): Unit = {
      val name = BimgurController.getClass.getSimpleName
      println(s"[$name] Error: $msg")
    }

  }

}
