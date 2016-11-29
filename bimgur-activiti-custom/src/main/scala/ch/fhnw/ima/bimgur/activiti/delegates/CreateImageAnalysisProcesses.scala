package ch.fhnw.ima.bimgur.activiti.delegates

import ammonite.ops._
import org.activiti.engine.delegate.{DelegateExecution, JavaDelegate}
import org.activiti.engine.impl.persistence.entity.VariableInstance
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

class CreateImageAnalysisProcesses extends JavaDelegate {

  private val log = LoggerFactory.getLogger(this.getClass)

  private val analysisNameVariableName = "analysis-name"
  private val workflowKeyVariableName = "workflow-key"
  private val imagePathVariableName = "image-path"
  private val imageFileVariableName = "image-file"

  private val serverDataDir = root / 'data

  override def execute(execution: DelegateExecution): Unit = {
    log.info("execute")
    val runtimeService = execution.getEngineServices.getRuntimeService

    for ((varName: String, varInstance: VariableInstance) <- execution.getVariableInstances.asScala) {
      log.info(s"Variable '$varName': '${varInstance.getValue}'")
    }

    val analysisName = execution.getVariableInstance(analysisNameVariableName).getTextValue
    val workflowKey = execution.getVariableInstance(workflowKeyVariableName).getTextValue
    val imagePath = execution.getVariableInstance(imagePathVariableName).getTextValue

    val serverImagePath = serverDataDir / imagePath
    log.info(s"Server image path: $serverImagePath")

    val absoluteImageFiles = ls ! serverImagePath
    for (absoluteImageFile: Path <- absoluteImageFiles) {
      log.info(s"Absolute image file: $absoluteImageFile")
      val imageFile = absoluteImageFile.relativeTo(serverDataDir)
      log.info(s"Relative image file: $imageFile")

      val processInstanceBuilder = runtimeService.createProcessInstanceBuilder
      processInstanceBuilder.processDefinitionKey(workflowKey)
      processInstanceBuilder.processInstanceName(analysisName)
      processInstanceBuilder.addVariable(analysisNameVariableName, analysisName)
      processInstanceBuilder.addVariable(imageFileVariableName, String.valueOf(imageFile))
      processInstanceBuilder.start
    }

  }

}
