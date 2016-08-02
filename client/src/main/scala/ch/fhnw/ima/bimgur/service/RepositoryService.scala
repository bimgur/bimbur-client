package ch.fhnw.ima.bimgur.service

import cats.data.Xor
import ch.fhnw.ima.bimgur._
import ch.fhnw.ima.bimgur.model.activiti.{ProcessDefinition, ProcessDefinitionKey, ProcessDefinitionList}
import io.circe.generic.auto._
import io.circe.Error
import io.circe.parser._
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

case class RepositoryService(activitiRestUrl: String) {

  val url = activitiRestUrl + "/repository"

  def getProcessDefinitions(key: ProcessDefinitionKey): Future[Xor[Error, Seq[ProcessDefinition]]] = {
    val requestFuture = Ajax.get(s"$url/process-definitions?key=$key")
    requestFuture.map(response => decode[ProcessDefinitionList](response.responseText).map(_.data))
  }

}
