package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.IntegrationTest;
import ch.fhnw.ima.bimgur.activiti.TestUtils;
import ch.fhnw.ima.bimgur.activiti.model.ProcessDefinitionId;
import ch.fhnw.ima.bimgur.activiti.model.ProcessInstance;
import ch.fhnw.ima.bimgur.activiti.model.StartProcessInstanceById;
import ch.fhnw.ima.bimgur.activiti.model.StartProcessInstanceByKey;
import io.reactivex.Observable;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;

@IntegrationTest
public class RuntimeServiceTest {

    private static RuntimeService runtimeService;

    @BeforeAll
    static void beforeAll() {
        runtimeService = TestUtils.client().getRuntimeService();

        TestUtils.resetDeployments();
        TestUtils.loadAndDeployTestDeployments(
                Collections.singletonList("demo-japanese-numbers.bpmn20.xml"));

        TestUtils.processEngine().getRuntimeService()
                .startProcessInstanceByKey("bimgur-demo-japanese-numbers");
    }

    @Test
    void startProcessInstanceByKey() {
        String processDefinitionKey = "bimgur-demo-japanese-numbers";
        StartProcessInstanceByKey startData = new StartProcessInstanceByKey(processDefinitionKey);
        Observable<ProcessInstance> result = RuntimeServiceTest.runtimeService.startProcessInstance(startData);
        result
                .map(ProcessInstance::getProcessDefinitionUrl)
                .test()
                .assertValue(url -> url.contains(processDefinitionKey));
    }

    @Test
    void startProcessInstanceById() {
        org.activiti.engine.RepositoryService repositoryService = TestUtils.processEngine().getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().list().get(0);
        ProcessDefinitionId processDefinitionId = new ProcessDefinitionId(processDefinition.getId());
        StartProcessInstanceById startData = new StartProcessInstanceById(processDefinitionId);
        Observable<ProcessInstance> result = RuntimeServiceTest.runtimeService.startProcessInstance(startData);


        result
                .map(ProcessInstance::getProcessDefinitionUrl)
                .test()
                .assertValue(url -> url.contains(processDefinition.getKey()));
    }


}
