package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.IntegrationTest;
import ch.fhnw.ima.bimgur.activiti.TestUtils;
import ch.fhnw.ima.bimgur.activiti.model.ProcessDefinition;
import ch.fhnw.ima.bimgur.activiti.model.ProcessDefinitionId;
import io.reactivex.Observable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;


@IntegrationTest
public class RepositoryServiceTest {
    private static RepositoryService service;


    @BeforeAll
    static void beforeAll() {
        service = TestUtils.client().getRepositoryService();
        TestUtils.resetDeployments();
        TestUtils.loadAndDeployTestDeployments(
                Collections.singletonList("bimgur-Integration-Test-RepositoryService.bpmn20.xml"));
    }

    @Test
    public void getProcessDefinition() throws Exception {
        Observable<ProcessDefinition> prcessDefinitoin = service.getProcessDefinition();
        prcessDefinitoin
                .toList()
                .map(List::size)
                .test().assertValue(1);
    }

    @Test
    public void getProcessDefinitionId() {
        Observable<ProcessDefinition> prcessDefinitoin = service.getProcessDefinition();
        String expectedProcessDefinitionId = "bimgur-Integration-Test-RepositoryService:1:";

        prcessDefinitoin
                .map(ProcessDefinition::getId)
                .map(ProcessDefinitionId::getRaw)
                .map(s -> s.substring(0, expectedProcessDefinitionId.length()))
                .test()
                .assertValue(expectedProcessDefinitionId);

    }

    @Test
    public void getProcessDefinitionKey() {
        Observable<ProcessDefinition> prcessDefinitoin = service.getProcessDefinition();
        prcessDefinitoin
                .map(ProcessDefinition::getKey)
                .test()
                .assertValue("bimgur-Integration-Test-RepositoryService");

    }

    @Test
    public void getProcessDefinitionCategory() {
        Observable<ProcessDefinition> prcessDefinitoin = service.getProcessDefinition();
        prcessDefinitoin
                .map(ProcessDefinition::getCategory)
                .test()
                .assertValue("http://www.activiti.org/processdef");

    }

    @Test
    public void getProcessDefinitionSuspended() {
        Observable<ProcessDefinition> prcessDefinitoin = service.getProcessDefinition();

        prcessDefinitoin
                .map(ProcessDefinition::getSuspended)
                .test()
                .assertValue(false);
    }

        @Test
    public void getProcessDefinitioName() {
        Observable<ProcessDefinition> prcessDefinitoin = service.getProcessDefinition();

        prcessDefinitoin
                .map(ProcessDefinition::getName)
                .test()
                .assertValue("IntegrationTest");
    }

    @Test
    public void getProcessDefinitioDescription() {
        Observable<ProcessDefinition> prcessDefinitoin = service.getProcessDefinition();

        prcessDefinitoin
                .map(ProcessDefinition::getDescription)
                .test()
                .assertValue("");
    }


}