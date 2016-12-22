package ch.fhnw.ima.bimgur.activiti.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class StartProcessInstanceByKey {

    private final String processDefinitionKey;

    public StartProcessInstanceByKey(@JsonProperty("processDefinitionKey") String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

}