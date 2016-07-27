package ch.fhnw.ima.bimgur.service

import ch.fhnw.ima.bimgur.model.activiti._
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future
import upickle.default._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

case class FormService(activitiRestUrl: String) {

  val url = activitiRestUrl + "/form/form-data"

  def getFormData(processDefinitionId: ProcessDefinitionId): Future[FormData] = {
    val urlWithParams = s"$url?processDefinitionId=$processDefinitionId"
    val requestFuture = Ajax.get(urlWithParams)
    requestFuture.map(response => read[FormData](response.responseText))
  }

}
