package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by fabrizio.parrillo on 22.12.2016.
 */
public class TaskFomDataDTO {
    @JsonProperty("taskId")
    private final String taskId;
    @JsonProperty("properties")
    private final List<FormProperty> formProperties;

    public TaskFomDataDTO(TaskId taskId, List<FormProperty> formProperties) {
        this.taskId = taskId.getRaw();
        this.formProperties = formProperties;
    }
}
