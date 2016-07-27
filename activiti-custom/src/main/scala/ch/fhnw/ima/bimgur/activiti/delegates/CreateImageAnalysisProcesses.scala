package ch.fhnw.ima.bimgur.activiti.delegates

import org.activiti.engine.delegate.{DelegateExecution, JavaDelegate}
import org.slf4j.LoggerFactory

class CreateImageAnalysisProcesses extends JavaDelegate {

  private val log = LoggerFactory.getLogger(this.getClass)

  override def execute(execution: DelegateExecution): Unit = {
    log.info("execute")
  }

}
