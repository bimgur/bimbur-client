package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class StartProcessInstanceByKeyDto {

    @JsonProperty("processDefinitionKey")
    private final String processDefinitionKey;

    public StartProcessInstanceByKeyDto(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

}