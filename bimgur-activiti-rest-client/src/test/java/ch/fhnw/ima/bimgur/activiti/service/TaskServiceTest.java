package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.TestUtils;
import ch.fhnw.ima.bimgur.activiti.model.*;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by fabrizio.parrillo on 23.12.2016.
 */
public class TaskServiceTest {

    private static TaskService taskService;


    @BeforeAll
    static void beforeAll() {
        taskService = TestUtils.client().getTaskService();

        TestUtils.resetDeployments();
        TestUtils.loadAndDeployTestDeployments(
                Collections.singletonList("demo-japanese-numbers.bpmn20.xml"));

        TestUtils.processEngine().getRuntimeService()
                .startProcessInstanceByKey("bimgur-demo-japanese-numbers");
    }

    @Test
    void getTasks() {
        taskService.getTasks()
                .elementAt(0)
                .map(Task::getId)
                .map(TaskId::getRaw)
                .test().assertResult(
                TestUtils.processEngine().getTaskService().createTaskQuery()
                        .list().get(0).getId()
        );
    }

    @Test
    void completeTask() {
        org.activiti.engine.task.Task task = TestUtils.processEngine().getTaskService().createTaskQuery().list().get(0);
        Observable<ResponseBody> testObserver = taskService
                .complete(
                        task.getId(),
                        new TaskCompleteDTO());
        testObserver.subscribe(responseBody -> {
            org.activiti.engine.task.Task task1 = TestUtils.processEngine().getTaskService().createTaskQuery().list().get(0);
            assertNotEquals(task, task1);
        });

        taskService
                .complete(
                        TestUtils.processEngine().getTaskService().createTaskQuery().list().get(0).getId(),
                        new TaskCompleteDTO())
                .test().assertComplete();

    }

    @Test
    void claimTask() {
        org.activiti.engine.task.Task task = TestUtils.processEngine().getTaskService().createTaskQuery().list().get(0);
        org.activiti.engine.identity.User user = TestUtils.processEngine().getIdentityService().createUserQuery().list().get(0);
        taskService.claim(task.getId(), new TaskClaimDTO(new UserId(user.getId())))
                .test()
                .assertComplete();


        org.activiti.engine.task.Task assigneedTask
                = TestUtils.processEngine().getTaskService().createTaskQuery().taskAssignee(user.getId()).list().get(0);

        assertEquals(task.getId(), assigneedTask.getId());

    }


}