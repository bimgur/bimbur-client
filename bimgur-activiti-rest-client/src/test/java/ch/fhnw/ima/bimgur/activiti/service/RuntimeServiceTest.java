package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.IntegrationTest;
import ch.fhnw.ima.bimgur.activiti.TestUtils;
import ch.fhnw.ima.bimgur.activiti.model.ProcessDefinitionId;
import ch.fhnw.ima.bimgur.activiti.model.ProcessInstance;
import ch.fhnw.ima.bimgur.activiti.model.StartProcessInstanceByIdDto;
import ch.fhnw.ima.bimgur.activiti.model.StartProcessInstanceByKeyDto;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

@IntegrationTest
public class RuntimeServiceTest {
    private static RuntimeService runtimeService;
    private static final ProcessEngine PROCESS_ENGINE = TestUtils.processEngine();


    @BeforeEach
    void beforeAll() {
        runtimeService = TestUtils.client().getRuntimeService();

        TestUtils.resetDeployments();
        TestUtils.loadAndDeployTestDeployments(
                Collections.singletonList("demo-japanese-numbers.bpmn20.xml"));

    }

    @Test
    void getProcessInstances() {
        runtimeService.getProcessInstances().test()
                .assertValueCount(0);

        PROCESS_ENGINE.getRuntimeService()
                .startProcessInstanceByKey(
                        PROCESS_ENGINE.getRepositoryService()
                                .createProcessDefinitionQuery().list().get(0)
                                .getKey()
                );

        runtimeService.getProcessInstances().test()
                .assertValueCount(1);
    }


    @Test
    void startProcessInstanceByKey() {
        String processDefinitionKey = PROCESS_ENGINE.getRepositoryService()
                .createProcessDefinitionQuery().list().get(0).getKey();

        StartProcessInstanceByKeyDto startDto = new StartProcessInstanceByKeyDto(processDefinitionKey);

        runtimeService.startProcessInstance(startDto)
                .map(ProcessInstance::getProcessDefinitionUrl)
                .test()
                .assertValue(url -> url.contains(processDefinitionKey));
    }

    @Test
    void startProcessInstanceById() {
        ProcessDefinition processDefinition =
                PROCESS_ENGINE.getRepositoryService().
                        createProcessDefinitionQuery().list().get(0);

        ProcessDefinitionId processDefinitionId = new ProcessDefinitionId(processDefinition.getId());
        StartProcessInstanceByIdDto startDto = new StartProcessInstanceByIdDto(processDefinitionId);


        runtimeService.startProcessInstance(startDto)
                .map(ProcessInstance::getProcessDefinitionUrl)
                .test()
                .assertValue(url -> url.contains(processDefinition.getKey()));
    }


}
