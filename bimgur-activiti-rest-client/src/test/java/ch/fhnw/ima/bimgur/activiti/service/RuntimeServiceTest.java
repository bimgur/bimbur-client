package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.IntegrationTest;
import ch.fhnw.ima.bimgur.activiti.TestUtils;
import ch.fhnw.ima.bimgur.activiti.model.ProcessInstance;
import ch.fhnw.ima.bimgur.activiti.model.StartProcessInstanceByKey;
import io.reactivex.Observable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@IntegrationTest
public class RuntimeServiceTest {

    private static RuntimeService runtimeService;

    @BeforeAll
    static void beforeAll() {
        runtimeService = TestUtils.client().getRuntimeService();
    }

    @Test
    void startProcessInstance() {
        String processDefinitionKey = "bimgur-demo-japanese-numbers";
        StartProcessInstanceByKey startData = new StartProcessInstanceByKey(processDefinitionKey);
        Observable<ProcessInstance> result = RuntimeServiceTest.runtimeService.startProcessInstance(startData);
        result
            .map(ProcessInstance::getProcessDefinitionUrl)
            .test()
            .assertValue(url -> url.contains(processDefinitionKey));
    }

}
