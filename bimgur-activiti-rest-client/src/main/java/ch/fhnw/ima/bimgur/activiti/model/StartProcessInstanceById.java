package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class StartProcessInstanceById {

    @JsonProperty("processDefinitionId")
    private final String processDefinitionId;

    public StartProcessInstanceById(ProcessDefinitionId id) {
        this.processDefinitionId = id.getRaw();
    }

}