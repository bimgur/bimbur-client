package ch.fhnw.ima.bimgur
package service

import cats.data.Xor
import ch.fhnw.ima.bimgur.model.activiti._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

case class RuntimeService(activitiRestUrl: String) {

  val url = activitiRestUrl + "/runtime"

  def getProcessInstances: Future[Xor[Error, Seq[ProcessInstance]]] = {
    val urlWithParams = s"$url/process-instances?includeProcessVariables=true"
    val requestFuture = Ajax.get(urlWithParams)
    requestFuture.map(response => decode[ProcessInstanceList](response.responseText).map(_.data))
  }

}
