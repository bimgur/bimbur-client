package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.IntegrationTest;
import ch.fhnw.ima.bimgur.activiti.TestUtils;
import ch.fhnw.ima.bimgur.activiti.model.FormData;
import ch.fhnw.ima.bimgur.activiti.model.FormDataByTaskIdDTO;
import ch.fhnw.ima.bimgur.activiti.model.FormProperty;
import ch.fhnw.ima.bimgur.activiti.model.TaskId;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * Created by fabrizio.parrillo on 23.12.2016.
 */
@IntegrationTest
public class FormDataServiceTest {

    private static FormDataService formDataService;
    private static final ProcessEngine PROCESS_ENGINE = TestUtils.processEngine();

    @BeforeAll
    static void beforeAll() {
        formDataService = TestUtils.client().getFormDataService();
        TestUtils.resetDeployments();
        TestUtils.loadAndDeployTestDeployments(
                Collections.singletonList("demo-japanese-numbers.bpmn20.xml"));

        PROCESS_ENGINE.getRuntimeService()
                .startProcessInstanceByKey("bimgur-demo-japanese-numbers");
    }

    @BeforeEach
    void beforeEach() {
        TestUtils.deleteAllProcessInstances();
        TestUtils.deleteAllTasks();

        PROCESS_ENGINE.getRuntimeService()
                .startProcessInstanceByKey("bimgur-demo-japanese-numbers");
    }

    @Test
    void getFormData() {

        Task task = PROCESS_ENGINE.getTaskService().createTaskQuery().list().get(0);
        org.activiti.engine.identity.User user = PROCESS_ENGINE.getIdentityService().createUserQuery().list().get(0);
        PROCESS_ENGINE.getTaskService().claim(
                task.getId(),
                user.getId()
        );


        formDataService.getFrom(new TaskId(task.getId()))
                .map(FormData::getFormProperties)
                .flatMap(formProperties -> io.reactivex.Observable.fromIterable(formProperties))
                .map(FormProperty::getName)
                .test().assertResult("FormProperty1", "FormProperty2");
    }

    @Test
    void addFormData() {

        Task task = PROCESS_ENGINE.getTaskService().createTaskQuery().list().get(0);
        org.activiti.engine.identity.User user = PROCESS_ENGINE.getIdentityService().createUserQuery().list().get(0);
        PROCESS_ENGINE.getTaskService().claim(
                task.getId(),
                user.getId()
        );

        Map<String, String> prop = new HashMap<>();
        prop.put("a", "1");
        prop.put("b", "2");

        List< Map<String, String>> proplist = new ArrayList<>();
        formDataService.addFormData(
                new FormDataByTaskIdDTO(new TaskId(task.getId()),
                        proplist
                )).test().assertComplete();

       /* formDataService.getFrom(new TaskId(task.getId()))
                .map(FormData::getFormProperties)
                .flatMap(formProperties -> io.reactivex.Observable.fromIterable(formProperties))
                .map(FormProperty::getName)
                .test().assertResult("FormProperty1", "FormProperty2");*/
    }

}