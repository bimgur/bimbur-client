package ch.fhnw.ima.bimgur.activiti.service;

import ch.fhnw.ima.bimgur.activiti.IntegrationTest;
import ch.fhnw.ima.bimgur.activiti.TestUtils;
import ch.fhnw.ima.bimgur.activiti.model.*;
import org.activiti.engine.ProcessEngine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Created by fabrizio.parrillo on 23.12.2016.
 */
@IntegrationTest
public class TaskServiceTest {

    private static TaskService taskService;
    private static final ProcessEngine PROCESS_ENGINE = TestUtils.processEngine();
    private org.activiti.engine.identity.User firstUserByProcessEngine;
    private org.activiti.engine.task.Task firstTaskByProcessEngine;

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

        this.firstUserByProcessEngine = PROCESS_ENGINE.getIdentityService().createUserQuery().list().get(0);
        this.firstTaskByProcessEngine = PROCESS_ENGINE.getTaskService().createTaskQuery().list().get(0);
    }

    @Test
    void getTasks() {
        taskService.getTasks()
                .elementAt(0)
                .map(Task::getId)
                .map(TaskId::getRaw)
                .test().assertResult(
                firstTaskByProcessEngine.getId()
        );
    }

    @Test
    void completeTask() {
        taskService
                .complete(
                        firstTaskByProcessEngine.getId(),
                        new TaskCompleteDTO())
                .test().assertComplete();

    }

    @Test
    void claimTask() {
        taskService.claim(
                firstTaskByProcessEngine.getId(),
                new TaskClaimDTO(new UserId(firstUserByProcessEngine.getId())))
                .test()
                .assertComplete();


        org.activiti.engine.task.Task assigneedTask
                = PROCESS_ENGINE.getTaskService().createTaskQuery().taskAssignee(firstUserByProcessEngine.getId()).list().get(0);

        assertEquals(
                firstTaskByProcessEngine.getId(),
                assigneedTask.getId()
        );


    }

    @Test
    void getFilteredTasksJustAssignee() {
        PROCESS_ENGINE.getTaskService().setAssignee(
                firstTaskByProcessEngine.getId(),
                firstUserByProcessEngine.getId()
        );

        taskService.getFilteredTasks(new UserId(firstUserByProcessEngine.getId()), null, null)
                .elementAt(0)
                .map(Task::getId)
                .map(TaskId::getRaw)
                .test()
                .assertResult(
                        firstTaskByProcessEngine.getId()
                );
    }

    @Test
    void getFilteredTasksByAllQuerryParameterAsAssignee() {
        PROCESS_ENGINE.getTaskService().setAssignee(
                firstTaskByProcessEngine.getId(),
                firstUserByProcessEngine.getId()
        );

        taskService
                .getFilteredTasks(
                        new UserId(firstUserByProcessEngine.getId()),
                        new ProcessInstanceId(firstTaskByProcessEngine.getProcessInstanceId()),
                        null
                )
                .elementAt(0)
                .map(Task::getId)
                .map(TaskId::getRaw)
                .test()
                .assertResult(firstTaskByProcessEngine.getId());
    }

    @Test
    void getFilteredTasksByAllQuerryParameterAsCanidate() {
        PROCESS_ENGINE.getTaskService()
                .addCandidateUser(
                        firstTaskByProcessEngine.getId(),
                        firstUserByProcessEngine.getId()
                );

        taskService
                .getFilteredTasks(
                        null,
                        new ProcessInstanceId(firstTaskByProcessEngine.getProcessInstanceId()),
                        new UserId(firstUserByProcessEngine.getId())
                )
                .elementAt(0)
                .map(Task::getId)
                .map(TaskId::getRaw)
                .test()
                .assertResult(firstTaskByProcessEngine.getId());
    }

    @Test
    void getTasksByAssignee() {
        PROCESS_ENGINE.getTaskService().setAssignee(
                firstTaskByProcessEngine.getId(),
                firstUserByProcessEngine.getId()
        );

        taskService.getTasksByAssignee(new UserId(firstUserByProcessEngine.getId()))
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
        PROCESS_ENGINE.getTaskService()
                .addCandidateUser(firstTaskByProcessEngine.getId(), firstUserByProcessEngine.getId());

        taskService.getTasksByCandidateUser(new UserId(firstUserByProcessEngine.getId()))
                .elementAt(0)
                .map(Task::getId)
                .map(TaskId::getRaw)
                .test()
                .assertResult(firstTaskByProcessEngine.getId());
    }

    @Test
    void getTasksByProcessInstanceId() {
        taskService
                .getTasksByProcessInstanceId(
                        new ProcessInstanceId(firstTaskByProcessEngine.getProcessInstanceId()))
                .elementAt(0)
                .map(Task::getId)
                .map(TaskId::getRaw)
                .test()
                .assertResult(firstTaskByProcessEngine.getId());
    }

}