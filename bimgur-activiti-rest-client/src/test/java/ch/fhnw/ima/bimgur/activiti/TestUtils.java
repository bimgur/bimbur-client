package ch.fhnw.ima.bimgur.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.repository.DeploymentBuilder;

import java.util.List;

public interface TestUtils {

    boolean IS_OSX = System.getProperty("os.name").toLowerCase().contains("mac");

    String HOST = IS_OSX ? "192.168.99.100" : "localhost";
    String BASE_URL = String.format("http://%s:8080/activiti-rest/service/", HOST);

    String USER_NAME = "kermit";
    String PASSWORD = "kermit";

    int DB_PORT = IS_OSX ? 5432 : 5433;
    String DB_URL = String.format("jdbc:postgresql://%s:%s/bimgurdb", HOST, DB_PORT);

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