package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class StartProcessInstanceByKey {

    @JsonProperty("processDefinitionKey")
    private final String processDefinitionKey;

    public StartProcessInstanceByKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

}