package ch.fhnw.ima.bimgur.service

import ch.fhnw.ima.bimgur.model.activiti._
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future
import upickle.default._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

case class RuntimeService(activitiRestUrl: String) {

  val url = activitiRestUrl + "/runtime"

  def getProcessInstances: Future[Seq[ProcessInstance]] = {
    val urlWithParams = s"$url/process-instances?includeProcessVariables=true"
    val requestFuture = Ajax.get(urlWithParams)
    requestFuture.map(response => read[ProcessInstanceList](response.responseText).data)
  }

}
