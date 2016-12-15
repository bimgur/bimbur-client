package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.IntegrationTest;
import ch.fhnw.ima.bimgur.activiti.TestUtils;
import ch.fhnw.ima.bimgur.activiti.model.ProcessDefinition;
import io.reactivex.Observable;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


@IntegrationTest
public class RepositoryServiceTest {
    private static RepositoryService service;

    @BeforeEach
    void beforeEach() {
        DeploymentBuilder deploymentBuilder = TestUtils.processEngine().getRepositoryService().createDeployment();
        deploymentBuilder.deploy();
    }

    @BeforeAll
    static void beforeAll() {
        service = TestUtils.client().getRepositoryServices();
    }

    @Test
    public void getProcessDefinition() throws Exception {
        Observable<ProcessDefinition> prcessDefinitoin = service.getProcessDefinition();
        prcessDefinitoin
                .map(ProcessDefinition::getId).subscribe(System.out::print);
    }

}