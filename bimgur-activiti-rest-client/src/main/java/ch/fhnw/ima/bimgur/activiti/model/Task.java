package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Task {

    private final TaskId id;
    private final ProcessInstanceId processInstanceId;
    private final String name;
    private final UserId assigneeId;

    @JsonCreator
    public Task(@JsonProperty("id") String id,
                @JsonProperty("processInstanceId") String processInstanceId,
                @JsonProperty("name") String name,
                @JsonProperty("assigneeId") String assigneeId) {

        this.id = new TaskId(id);
        this.processInstanceId = new ProcessInstanceId(processInstanceId);
        this.name = name;
        this.assigneeId = new UserId(assigneeId);
    }

    public TaskId getId() {
        return id;
    }

    public ProcessInstanceId getProcessInstanceId() {
        return processInstanceId;
    }

    public String getName() {
        return name;
    }

    public UserId getAssigneeId() {
        return assigneeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return this.id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + id;
    }

}
