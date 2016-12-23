package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by fabrizio.parrillo on 22.12.2016.
 */
public class TaskCompleteDTO {
    @JsonProperty("action")
    private final String action = "complete";

    public TaskCompleteDTO() {

    }
}
