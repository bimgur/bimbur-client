package ch.fhnw.ima.bimgur.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.repository.DeploymentBuilder;

import java.util.List;

public interface TestUtils {

    String HOST = "localhost"; // TODO: make configurable on OSX
    String BASE_URL = "http://" + HOST + ":4444/activiti-rest/service/";
    String USER_NAME = "kermit";
    String PASSWORD = "kermit";
    String DB_URL = "jdbc:postgresql://" + HOST + ":5433/bimgurdb";

    static ActivitiRestClient client() {
        return ActivitiRestClient.connect(BASE_URL, USER_NAME, PASSWORD);
    }

    static ProcessEngine processEngine() {
        return ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault()
                .setJdbcUrl(DB_URL)
                .buildProcessEngine();
    }

    static void resetDeployments() {

        TestUtils.processEngine().
                getRepositoryService().createDeploymentQuery().list()
                .forEach(deployment ->
                        TestUtils.processEngine()
                                .getRepositoryService()
                                .deleteDeployment(deployment.getId(), true)
                );

    }

    static void deleteAllTasks() {
        TestUtils.processEngine().getTaskService().createTaskQuery().list()
                .forEach(task -> TestUtils.processEngine().getTaskService().deleteTask(task.getId(), true));
    }

    static void deleteAllProcessInstances() {
        TestUtils.processEngine().getRuntimeService()
                .createProcessInstanceQuery().list()
                .forEach(pi -> TestUtils.processEngine().getRuntimeService().deleteProcessInstance(pi.getProcessInstanceId(), null));
    }

    static void loadAndDeployTestDeployments(List<String> workflowResources) {
        DeploymentBuilder deploymentBuilder = TestUtils.processEngine().getRepositoryService().createDeployment();
        workflowResources.forEach(
                deploymentBuilder::addClasspathResource
        );
        deploymentBuilder.deploy();
    }


}
