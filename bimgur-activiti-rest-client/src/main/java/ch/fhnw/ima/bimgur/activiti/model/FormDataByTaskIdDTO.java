package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by fabrizio.parrillo on 23.12.2016.
 */
public class FormDataByTaskIdDTO {
    @JsonProperty("taskId")
    private final String taskId;
    @JsonProperty("properties")
    private final List<Map<String, String>> properties;

    public FormDataByTaskIdDTO(TaskId taskId, List<Map<String, String>> properties ) {
        this.taskId = taskId.getRaw();
        this.properties = properties;
    }
}
