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
    private static RepositoryService repositoryService;


    @BeforeAll
    static void beforeAll() {
        repositoryService = TestUtils.client().getRepositoryService();
        TestUtils.resetDeployments();
        TestUtils.loadAndDeployTestDeployments(
                Collections.singletonList("bimgur-Integration-Test-RepositoryService.bpmn20.xml"));
    }

    @Test
    public void getProcessDefinition() throws Exception {
        Observable<ProcessDefinition> prcessDefinitoin = repositoryService.getProcessDefinition();
        prcessDefinitoin
                .toList()
                .map(List::size)
                .test().assertValue(1);
    }

    @Test
    public void getProcessDefinitionId() {
        String expectedProcessDefinitionId =
                getFirstProcessDefintionByActivityProcessEngine().getId();

        repositoryService.getProcessDefinition()
                .elementAt(0)
                .map(ProcessDefinition::getId)
                .map(ProcessDefinitionId::getRaw)
                .test()
                .assertValue(expectedProcessDefinitionId);
    }

    @Test
    public void getProcessDefinitionKey() {
        String expectedProcessDefinitionKey =
                getFirstProcessDefintionByActivityProcessEngine().getKey();

        repositoryService.getProcessDefinition()
                .map(ProcessDefinition::getKey)
                .test()
                .assertValue(expectedProcessDefinitionKey);
    }

    @Test
    public void getProcessDefinitionCategory() {
        String expectedCategory =
                getFirstProcessDefintionByActivityProcessEngine().getCategory();

        repositoryService.getProcessDefinition()
                .map(ProcessDefinition::getCategory)
                .test()
                .assertValue(expectedCategory);

    }

    @Test
    public void getProcessDefinitionSuspended() {
        Boolean expectedSuspended =
                getFirstProcessDefintionByActivityProcessEngine().isSuspended();

        repositoryService.getProcessDefinition()
                .map(ProcessDefinition::getSuspended)
                .test()
                .assertValue(expectedSuspended);
    }

    @Test
    public void getProcessDefinitioName() {
        String expectedName =
                getFirstProcessDefintionByActivityProcessEngine().getName();

        repositoryService.getProcessDefinition()
                .map(ProcessDefinition::getName)
                .test()
                .assertValue(expectedName);
    }


    private static org.activiti.engine.repository.ProcessDefinition
    getFirstProcessDefintionByActivityProcessEngine() {
        return TestUtils.processEngine()
                .getRepositoryService().createProcessDefinitionQuery()
                .list().get(0);
    }
}