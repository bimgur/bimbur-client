package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by fabrizio.parrillo on 23.12.2016.
 */
public class FormDataByTaskIdDTO {
    @JsonProperty("taskId")
    private final String taskId;

    public FormDataByTaskIdDTO(String taskId) {
        this.taskId = taskId;
    }
}
