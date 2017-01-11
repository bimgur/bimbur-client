package ch.fhnw.ima.bimgur.client.model;

import ch.fhnw.ima.bimgur.activiti.model.ProcessInstance;
import ch.fhnw.ima.bimgur.activiti.model.Task;
import ch.fhnw.ima.bimgur.activiti.model.TaskId;
import ch.fhnw.ima.bimgur.activiti.model.User;

/**
 * In contrast to its {@link Task} counter part, this task has fully populated fields, e.g. its assignee field
 * references an actual {@link User} rather than just a {@link ch.fhnw.ima.bimgur.activiti.model.UserId}.
 */
public final class RichTask {

    private final Task task;
    private final User assignee;
    private final ProcessInstance processInstance;

    public RichTask(Task task, User assignee, ProcessInstance processInstance) {
        this.task = task;
        this.assignee = assignee;
        this.processInstance = processInstance;
    }

    public TaskId getId() {
        return task.getId();
    }

    public String getName() {
        return task.getName();
    }

    public User getAssignee() {
        return assignee;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

}