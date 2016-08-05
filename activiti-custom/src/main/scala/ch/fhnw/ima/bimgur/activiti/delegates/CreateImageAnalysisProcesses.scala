package ch.fhnw.ima.bimgur.activiti.delegates

import org.activiti.engine.delegate.{DelegateExecution, JavaDelegate}
import org.activiti.engine.impl.persistence.entity.VariableInstance
import org.slf4j.LoggerFactory
import scala.collection.JavaConversions._

class CreateImageAnalysisProcesses extends JavaDelegate {

  private val log = LoggerFactory.getLogger(this.getClass)

  override def execute(execution: DelegateExecution): Unit = {
    log.info("execute")

    for ((varName: String, varInstance: VariableInstance) <- execution.getVariableInstances) {
      log.info(s"Variable '$varName': '${varInstance.getValue}'")
    }

  }

}
