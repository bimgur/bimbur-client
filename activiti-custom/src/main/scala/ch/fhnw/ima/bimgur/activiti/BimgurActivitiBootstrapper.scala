package ch.fhnw.ima.bimgur.activiti

import javax.servlet.{ServletContextEvent, ServletContextListener}

import org.activiti.engine.ProcessEngines
import org.slf4j.LoggerFactory

class BimgurActivitiBootstrapper extends ServletContextListener {

  private val log = LoggerFactory.getLogger(this.getClass)

  private val workflows = List("master-workflow", "demo-japanese-numbers")

  override def contextInitialized(sce: ServletContextEvent): Unit = {
    log.info("Initializing process engine")
    val engine = ProcessEngines.getDefaultProcessEngine
    log.info("Deploying workflows")
    val deployment = engine.getRepositoryService.createDeployment()
    for (workflow <- workflows) {
      deployment.addClasspathResource(workflow + ".bpmn20.xml")
    }
    deployment.deploy()
  }

  override def contextDestroyed(sce: ServletContextEvent): Unit = {
    // no tear down needed
  }

}
