package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class StartProcessInstanceByIdDto {

    @JsonProperty("processDefinitionId")
    private final String processDefinitionId;

    public StartProcessInstanceByIdDto(ProcessDefinitionId id) {
        this.processDefinitionId = id.getRaw();
    }

}