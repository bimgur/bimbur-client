package ch.fhnw.ima.bimgur.activiti

import javax.servlet.{ServletContextEvent, ServletContextListener}

import org.activiti.engine.ProcessEngines
import org.slf4j.LoggerFactory

class BimgurActivitiBootstrapper extends ServletContextListener {

  private val log = LoggerFactory.getLogger(this.getClass)
  private val masterWorkflowResource: String = "master-workflow.bpmn20.xml"

  override def contextInitialized(sce: ServletContextEvent): Unit = {
    log.info("Initializing process engine")
    val engine = ProcessEngines.getDefaultProcessEngine
    log.info("Deploying master workflow")
    engine.getRepositoryService.createDeployment().
      addClasspathResource(masterWorkflowResource)
      .deploy()
  }

  override def contextDestroyed(sce: ServletContextEvent): Unit = {
    // no tear down needed
  }

}
