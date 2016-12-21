package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.IntegrationTest;
import ch.fhnw.ima.bimgur.activiti.TestUtils;
import ch.fhnw.ima.bimgur.activiti.model.ProcessDefinitionId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;


@IntegrationTest
public class RuntimeServiceTest {
    private static RuntimeService runtimeService;
    private static RepositoryService repositoryService;

    @BeforeAll
    static void beforeAll() {
        runtimeService = TestUtils.client().getRuntimeService();
        repositoryService = TestUtils.client().getRepositoryService();

        TestUtils.resetDeployments();
        TestUtils.loadAndDeployTestDeployments(
                Collections.singletonList("demo-japanese-numbers.bpmn20.xml"));

        TestUtils.processEngine().getRuntimeService()
                .startProcessInstanceByKey("bimgur-demo-japanese-numbers");

    }


/*    @Test
    void getProcessInstances() {
        runtimeService.getProcessInstances()
                .map(ProcessInstance::getId)
                .subscribe(System.out::println);
    }


    @Test
    void getTasks() {
        runtimeService.getTasks()
                .map(Task::getId)
                .subscribe(System.out::println);
    }*/

    @Test
    void startProcessInstance() {
        ProcessDefinitionId processDefinitionId = repositoryService.getProcessDefinition().lastElement().blockingGet().getId();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employeeName", "Kermit");
        variables.put("numberOfDays", new Integer(4));
        variables.put("vacationMotivation", "I'm really tired!");

        org.activiti.engine.RuntimeService runtimeService1 = TestUtils.processEngine().getRuntimeService();

        runtimeService1
                .startProcessInstanceByKey("bimgur-demo-japanese-numbers", variables);
        runtimeService1
                .startProcessInstanceByKey("bimgur-demo-japanese-numbers", variables);

        System.out.println(runtimeService1.createProcessInstanceQuery().count());


        runtimeService.startProcessInstance("bimgur-demo-japanese-numbers");
        runtimeService.startProcessInstance(processDefinitionId.getRaw());
        runtimeService.startProcessInstance(processDefinitionId.getRaw());


        assertTrue(runtimeService1.createProcessInstanceQuery().count() == 5 );


    }
}
