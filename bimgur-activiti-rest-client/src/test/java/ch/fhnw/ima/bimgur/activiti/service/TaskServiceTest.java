package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.TestUtils;
import ch.fhnw.ima.bimgur.activiti.model.*;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import org.activiti.engine.ProcessEngine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by fabrizio.parrillo on 23.12.2016.
 */
public class TaskServiceTest {

    private static TaskService taskService;
    private static final ProcessEngine PROCESS_ENGINE = TestUtils.processEngine();


    @BeforeAll
    static void beforeAll() {
        taskService = TestUtils.client().getTaskService();

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
    void getTasks() {
        taskService.getTasks()
                .elementAt(0)
                .map(Task::getId)
                .map(TaskId::getRaw)
                .test().assertResult(
                PROCESS_ENGINE.getTaskService().createTaskQuery()
                        .list().get(0).getId()
        );
    }

    @Test
    void completeTask() {
        org.activiti.engine.task.Task task = PROCESS_ENGINE.getTaskService().createTaskQuery().list().get(0);
        Observable<ResponseBody> testObserver = taskService
                .complete(
                        task.getId(),
                        new TaskCompleteDTO());
        testObserver.subscribe(responseBody -> {
            org.activiti.engine.task.Task task1 = PROCESS_ENGINE.getTaskService().createTaskQuery().list().get(0);
            assertNotEquals(task, task1);
        });

        taskService
                .complete(
                        PROCESS_ENGINE.getTaskService().createTaskQuery().list().get(0).getId(),
                        new TaskCompleteDTO())
                .test().assertComplete();

    }

    @Test
    void claimTask() {
        org.activiti.engine.task.Task task = PROCESS_ENGINE.getTaskService().createTaskQuery().list().get(0);
        org.activiti.engine.identity.User user = PROCESS_ENGINE.getIdentityService().createUserQuery().list().get(0);
        taskService.claim(task.getId(), new TaskClaimDTO(new UserId(user.getId())))
                .test()
                .assertComplete();


        org.activiti.engine.task.Task assigneedTask
                = PROCESS_ENGINE.getTaskService().createTaskQuery().taskAssignee(user.getId()).list().get(0);

        assertEquals(task.getId(), assigneedTask.getId());



    }

    @Test
    void getFilteredTasksJustAssignee() {
        org.activiti.engine.identity.User user = PROCESS_ENGINE.getIdentityService().createUserQuery().list().get(0);
        PROCESS_ENGINE.getTaskService().setAssignee(
                PROCESS_ENGINE.getTaskService().createTaskQuery().list().get(0).getId(),
                user.getId()
        );

        taskService.getFilteredTasks(new UserId(user.getId()), null, null)
                .elementAt(0)
                .map(Task::getId)
                .map(TaskId::getRaw)
                .test().assertResult(
                PROCESS_ENGINE.getTaskService().createTaskQuery()
                        .list().get(0).getId()
        );
    }

    @Test
    void getFilteredTasksByAllQuerryParameterAsAssignee() {
        org.activiti.engine.identity.User user = PROCESS_ENGINE.getIdentityService().createUserQuery().list().get(0);
        org.activiti.engine.task.Task task = PROCESS_ENGINE.getTaskService().createTaskQuery().list().get(0);

        PROCESS_ENGINE.getTaskService().setAssignee(
                PROCESS_ENGINE.getTaskService().createTaskQuery().list().get(0).getId(),
                user.getId()
        );

        taskService
                .getFilteredTasks(
                        new UserId(user.getId()),
                        new ProcessInstanceId(task.getProcessInstanceId()),
                        null
                )
                .elementAt(0)
                .map(Task::getId)
                .map(TaskId::getRaw)
                .test()
                .assertResult(task.getId());
    }

    @Test
    void getFilteredTasksByAllQuerryParameterAsCanidate() {
        org.activiti.engine.identity.User user = PROCESS_ENGINE.getIdentityService().createUserQuery().list().get(0);
        org.activiti.engine.task.Task task = PROCESS_ENGINE.getTaskService().createTaskQuery().list().get(0);


        PROCESS_ENGINE.getTaskService()
                .addCandidateUser(task.getId(), user.getId());

        taskService
                .getFilteredTasks(
                        null,
                        new ProcessInstanceId(task.getProcessInstanceId()),
                        new UserId(user.getId())
                )
                .elementAt(0)
                .map(Task::getId)
                .map(TaskId::getRaw)
                .test()
                .assertResult(task.getId());


    }

    @Test
    void getTasksByAssignee() {
        org.activiti.engine.identity.User user = PROCESS_ENGINE.getIdentityService().createUserQuery().list().get(0);
        PROCESS_ENGINE.getTaskService().setAssignee(
                PROCESS_ENGINE.getTaskService().createTaskQuery().list().get(0).getId(),
                user.getId()
        );

        taskService.getTasksByAssignee(new UserId(user.getId()))
                .elementAt(0)
                .map(Task::getId)
                .map(TaskId::getRaw)
                .test().assertResult(
                PROCESS_ENGINE.getTaskService().createTaskQuery()
                        .list().get(0).getId()
        );
    }

    @Test
    void getTasksByCandidateUser() {
        org.activiti.engine.identity.User user = PROCESS_ENGINE.getIdentityService().createUserQuery().list().get(0);
        org.activiti.engine.task.Task task = PROCESS_ENGINE.getTaskService().createTaskQuery().list().get(0);
        PROCESS_ENGINE.getTaskService()
                .addCandidateUser(task.getId(), user.getId());

        taskService.getTasksByCandidateUser(new UserId(user.getId()))
                .elementAt(0)
                .map(Task::getId)
                .map(TaskId::getRaw)
                .test()
                .assertResult(task.getId());
    }

    @Test
    void getTasksByProcessInstanceId() {
        org.activiti.engine.task.Task task = PROCESS_ENGINE.getTaskService().createTaskQuery().list().get(0);

        taskService.getTasksByProcessInstanceId(new ProcessInstanceId(task.getProcessInstanceId()))
                .elementAt(0)
                .map(Task::getId)
                .map(TaskId::getRaw)
                .test()
                .assertResult(task.getId());
    }

}